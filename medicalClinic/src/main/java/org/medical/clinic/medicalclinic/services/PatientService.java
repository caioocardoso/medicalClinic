package org.medical.clinic.medicalclinic.services;

import org.medical.clinic.medicalclinic.DTO.patient.PatientDTO;
import org.medical.clinic.medicalclinic.DTO.patient.PatientProfileDTO;
import org.medical.clinic.medicalclinic.DTO.patient.PatientRegistrationData;
import org.medical.clinic.medicalclinic.DTO.patient.PatientUpdateData;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.models.RoleType;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public PatientDTO createPatientProfile(User user, PatientRegistrationData patientData) {
        if(repository.existsByUser(user)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já é um paciente");
        }
        if(repository.existsByCpf(patientData.cpf())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setCpf(patientData.cpf());
        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientUpdateData data) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

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
        return repository.findAll(pageable).map(PatientDTO::new);
    }

    public PatientProfileDTO getPatientProfileByUserId(Long userId) {
        Patient patient = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
        return new PatientProfileDTO(patient);
    }

    @Transactional
    public PatientDTO deletePatient(Long id) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        patient.setActive(false);
        repository.save(patient);
        return new PatientDTO(patient);
    }

    @Transactional
    public PatientDTO addPatientProfileToExistingUser(User user, String cpf) {
        if (repository.existsByUser(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já é um paciente");
        }
        if (repository.existsByCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cpf já cadastrado");
        }
        user.getRoles().add(RoleType.ROLE_PATIENT);

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setCpf(cpf);

        userRepository.save(user);
        Patient saved = repository.save(patient);
        return new PatientDTO(saved);
    }
}