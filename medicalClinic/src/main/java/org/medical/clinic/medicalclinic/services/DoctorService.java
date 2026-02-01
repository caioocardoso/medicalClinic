package org.medical.clinic.medicalclinic.services;

import jakarta.transaction.Transactional;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.DoctorRequestRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    DoctorRequestRepository doctorRequestRepository;

    @Transactional
    public DoctorDTO createDoctorProfile(User user, DoctorRegistrationData doctorData) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setCrm(doctorData.crm());
        doctor.setSpeciality(doctorData.speciality());
        Doctor saved = doctorRepository.save(doctor);
        return new DoctorDTO(saved);
    }

    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorUpdateData data){
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        User user = doctor.getUser();

        if(data.name() != null && !data.name().isBlank()){
            user.setName(data.name());
        }
        if(data.phone() != null && !data.phone().isBlank()){
            user.setPhone(data.phone());
        }
        if(data.address() != null){
            user.setAddress(new Address(data.address()));
        }

        doctorRepository.save(doctor);
        return new DoctorDTO(doctor);
    }

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAllByActiveTrue(pageable).map(DoctorDTO::new);
    }

    @Transactional
    public DoctorDTO deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
        return new DoctorDTO(doctor);
    }

    public DoctorRequestDTO saveDoctorRequest (DoctorRequest doctorRequest){
        if(doctorRequestRepository.existsByUserAndFinishedFalse(doctorRequest.getUser())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a pending doctor request for this user");
        }
        if(doctorRepository.existsByCrm(doctorRequest.getDoctorRegistrationData().crm())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A doctor with this CRM already exists");
        }
        DoctorRequest savedEntity = doctorRequestRepository.save(doctorRequest);

        return new DoctorRequestDTO(savedEntity);
    }

    public DoctorRequestDTO approveDoctorRequest(ApprovalDoctorRequest approvalData){
        DoctorRequest doctorRequest = doctorRequestRepository.findById(approvalData.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor request not found"));

        if (doctorRequest.isFinished()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor request has already been processed");
        }

        if(doctorRepository.existsByCrm(doctorRequest.getDoctorRegistrationData().crm())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A doctor with this CRM already exists");
        }

        User user = doctorRequest.getUser();

        if(approvalData.isApproved()){
            if(user.getRoles().contains(RoleType.ROLE_DOCTOR) || doctorRepository.existsByUser(user)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a doctor");
            }

            createDoctorProfile(user, doctorRequest.getDoctorRegistrationData());
            user.getRoles().add(RoleType.ROLE_DOCTOR);
            userRepository.save(user);
            doctorRequest.setAccepted(true);
        }else doctorRequest.setAccepted(false);

        doctorRequest.setFinished(true);
        doctorRequestRepository.save(doctorRequest);
        return new DoctorRequestDTO(doctorRequest);
    }

    public Page<DoctorRequestDTO> listDoctorRequests(Pageable pageable) {
        return doctorRequestRepository.findAll(pageable).map(DoctorRequestDTO::new);
    }

    @Transactional
    public DoctorDTO addDoctorProfileToUserById(Long userId, DoctorRegistrationData data) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (doctorRepository.existsByUser(targetUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a doctor");
        }

        Doctor doctor = new Doctor();
        doctor.setUser(targetUser);
        doctor.setCrm(data.crm());
        doctor.setSpeciality(data.speciality());

        targetUser.getRoles().add(RoleType.ROLE_DOCTOR);

        userRepository.save(targetUser);

        return new DoctorDTO(doctorRepository.save(doctor));
    }
}