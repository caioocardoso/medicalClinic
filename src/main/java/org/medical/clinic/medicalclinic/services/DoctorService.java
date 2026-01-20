package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.DTO.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.DTO.DoctorUpdateData;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DoctorService {
    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public DoctorDTO newDoctor(@Valid DoctorRegistrationData data){
        Doctor doctor = new Doctor(data);
        Doctor saved = repository.save(doctor);
        return new DoctorDTO(saved);
    }

    public DoctorDTO updateDoctor(Long id, DoctorUpdateData data){
        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        if(data.name() != null && !data.name().isBlank()){
            doctor.setName(data.name());
        }
        if(data.phone() != null && !data.phone().isBlank()){
            doctor.setPhone(data.phone());
        }
        if(data.address() != null){
            doctor.setAddress(new Address(data.address()));
        }

        Doctor saved = repository.save(doctor);
        return new DoctorDTO(saved);
    }

    public Page<DoctorDTO> getAllDoctors(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(DoctorDTO::new);
    }

    public DoctorDTO deleteDoctor(Long id) {
        Doctor doctor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
        doctor.setActive(false);
        Doctor saved = repository.save(doctor);
        return new DoctorDTO(saved);
    }
}
