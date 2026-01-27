package org.medical.clinic.medicalclinic.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.DTO.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.DTO.DoctorUpdateData;
import org.medical.clinic.medicalclinic.models.Speciality;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository repository;

    @Transactional
    public DoctorDTO createDoctorProfile(User user, String crm, Speciality speciality) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setCrm(crm);
        doctor.setSpeciality(speciality);
        Doctor saved = repository.save(doctor);
        return new DoctorDTO(saved);
    }

    @Transactional
    public DoctorDTO updateDoctor(Long id, DoctorUpdateData data){
        Doctor doctor = repository.findById(id)
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

        repository.save(doctor);
        return new DoctorDTO(doctor);
    }

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(DoctorDTO::new);
    }

    @Transactional
    public DoctorDTO deleteDoctor(Long id) {
        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
        doctor.setActive(false);
        repository.save(doctor);
        return new DoctorDTO(doctor);
    }
}