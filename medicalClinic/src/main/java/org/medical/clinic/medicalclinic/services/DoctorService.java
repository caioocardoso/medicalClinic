package org.medical.clinic.medicalclinic.services;

import jakarta.transaction.Transactional;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.DoctorRequestRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public DoctorDTO updateDoctor(Long id, DoctorUpdateData data) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));

        User user = doctor.getUser();

        if (data.name() != null && !data.name().isBlank()) {
            user.setName(data.name());
        }
        if (data.phone() != null && !data.phone().isBlank()) {
            user.setPhone(data.phone());
        }
        if (data.address() != null) {
            user.setAddress(new Address(data.address()));
        }

        doctorRepository.save(doctor);
        return new DoctorDTO(doctor);
    }

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(DoctorDTO::new);
    }

    public DoctorDTO getDoctorDtoById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
        return new DoctorDTO(doctor);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
    }

    public DoctorProfileDTO getDoctorProfileByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
        return new DoctorProfileDTO(doctor);
    }

    @Transactional
    public DoctorDTO deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
        return new DoctorDTO(doctor);
    }

    public DoctorRequestDTO saveDoctorRequest(DoctorRequest doctorRequest) {
        if (doctorRequestRepository.existsByUserAndFinishedFalse(doctorRequest.getUser())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma solicitação de médico pendente para este usuário");
        }
        if (doctorRepository.existsByCrm(doctorRequest.getDoctorRegistrationData().crm())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um médico com este CRM");
        }
        DoctorRequest savedEntity = doctorRequestRepository.save(doctorRequest);

        return new DoctorRequestDTO(savedEntity);
    }

    @Transactional
    public DoctorRequestDTO registerNewDoctorRequest(FullDoctorRegistrationDTO data) {
        if (userRepository.findByEmail(data.userData().getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já está em uso");
        }

        User newUser = new User();
        newUser.setEmail(data.userData().getEmail());
        newUser.setName(data.userData().getName());
        newUser.setPhone(data.userData().getPhone());
        newUser.setAddress(new Address(data.userData().getAddress()));
        newUser.setPassword(new BCryptPasswordEncoder().encode(data.userData().getPassword()));

        User savedUser = userRepository.save(newUser);

        DoctorRequest doctorRequest = new DoctorRequest(savedUser, data.doctorData());

        return saveDoctorRequest(doctorRequest);
    }

    public DoctorRequestDTO approveDoctorRequest(ApprovalDoctorRequest approvalData) {
        DoctorRequest doctorRequest = doctorRequestRepository.findById(approvalData.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação de médico não encontrada"));

        if (!approvalData.isApproved())
            doctorRequest.setAccepted(false);
        else {
            if (doctorRequest.isFinished()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Solicitação de médico já foi processada");
            }

            if (doctorRepository.existsByCrm(doctorRequest.getDoctorRegistrationData().crm())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um médico com este CRM");
            }

            User user = doctorRequest.getUser();
            if (user.getRoles().contains(RoleType.ROLE_DOCTOR) || doctorRepository.existsByUser(user)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já é um médico");
            }

            createDoctorProfile(user, doctorRequest.getDoctorRegistrationData());
            user.getRoles().add(RoleType.ROLE_DOCTOR);
            userRepository.save(user);
            doctorRequest.setAccepted(true);
        }

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (doctorRepository.existsByUser(targetUser)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já é um médico");
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