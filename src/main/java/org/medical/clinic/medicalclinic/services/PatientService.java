package org.medical.clinic.medicalclinic.services;

import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.models.RoleType;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatientService {
    @Autowired
    private PatientRepository repository;

    @Transactional
    public PatientDTO createPatientProfile(User user, PatientRegistrationData patientData) {
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setCpf(patientData.cpf());
        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientUpdateData data) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        User user = patient.getUser();

        if (data.name() != null && !data.name().isBlank()) {
            user.setName(data.name());
        }
        if (data.phone() != null && !data.phone().isBlank()) {
            user.setPhone(data.phone());
        }
        if (data.address() != null) {
            user.setAddress(new Address(data.address()));
        }

        repository.save(patient);
        return new PatientDTO(patient);
    }

    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(PatientDTO::new);
    }

    @Transactional
    public PatientDTO deletePatient(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patient.setActive(false);
        repository.save(patient);
        return new PatientDTO(patient);
    }

    @Transactional
    public PatientDTO addPatientProfileToExistingUser(User user, String cpf) {
        if (repository.existsByUser(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a patient");
        }

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setCpf(cpf);

        user.getRoles().add(RoleType.ROLE_PATIENT);

        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }
}