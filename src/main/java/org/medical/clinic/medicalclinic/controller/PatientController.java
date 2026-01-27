package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.medical.clinic.medicalclinic.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/paciente")
@Validated
public class PatientController {
    @Autowired
    PatientService service;

    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatients(
            @PageableDefault(size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PatientDTO> patients = service.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientUpdateData updatePatient) {
        PatientDTO updated = service.updatePatient(id, updatePatient);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PatientDTO> deletePatient(@PathVariable Long id){
        PatientDTO deleted = service.deletePatient(id);
        return ResponseEntity.ok(deleted);
    }
}