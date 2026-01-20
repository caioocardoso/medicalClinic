package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public PatientDTO newPatient(@Valid PatientRegistrationData data) {
        Patient patient = new Patient(data);
        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }

    public PatientDTO updatePatient(Long id, PatientUpdateData data){
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        if(data.name() != null && !data.name().isBlank()){
            patient.setName(data.name());
        }
        if(data.phone() != null && !data.phone().isBlank()){
            patient.setPhone(data.phone());
        }
        if(data.address() != null){
            patient.setAddress(new Address(data.address()));
        }

        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }

    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(PatientDTO::new);
    }

    public PatientDTO deletePatient(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        patient.setActive(false);
        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }
}
