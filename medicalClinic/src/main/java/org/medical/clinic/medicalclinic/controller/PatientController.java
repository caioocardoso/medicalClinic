package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.clients.EmailClient;
import org.medical.clinic.medicalclinic.clients.EmailDto;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/paciente")
@Validated
public class PatientController {
    @Autowired
    PatientService service;
    @Autowired
    private EmailClient emailClient;

    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @PageableDefault(size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PatientDTO> patients = service.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/me")
    public ResponseEntity<PatientProfileDTO> getMyData(@AuthenticationPrincipal User user) {
        PatientProfileDTO patient = service.getPatientProfileByUserId(user.getId());
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateData updatePatient) {
        PatientDTO updated = service.updatePatient(id, updatePatient);

        EmailDto email = new EmailDto(updated.getEmail(), "Seu cadastro de paciente foi atualizado com sucesso!", "Olá " + updated.getName() + ", seus dados de paciente foram atualizados com sucesso no nosso sistema.");
        emailClient.sendEmail(email);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable Long id){
        PatientDTO deleted = service.deletePatient(id);

        EmailDto email = new EmailDto(deleted.getEmail(), "Seu cadastro de paciente foi removido!", "Olá " + deleted.getName() + ", seu cadastro de paciente foi removido do nosso sistema. Se tiver alguma dúvida, entre em contato conosco.");
        emailClient.sendEmail(email);

        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/perfil")
    public ResponseEntity<PatientDTO> addPatientProfile(
            @RequestBody @Valid PatientRegistrationData data,
            @AuthenticationPrincipal User user) {

        PatientDTO patientDTO = service.addPatientProfileToExistingUser(user, data.cpf());

        EmailDto email = new EmailDto(patientDTO.getEmail(), "Perfil de paciente criado com sucesso!", "Olá " + patientDTO.getName() + ", seu perfil de paciente foi criado com sucesso no nosso sistema.");
        emailClient.sendEmail(email);

        return ResponseEntity.status(HttpStatus.CREATED).body(patientDTO);
    }
}