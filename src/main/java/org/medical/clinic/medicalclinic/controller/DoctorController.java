package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.models.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.models.DoctorUpdateData;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/medico")
@Validated
public class DoctorController {
    DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> newDoctor(@Valid @RequestBody DoctorRegistrationData newDoctor) {
        DoctorDTO saved = service.newDoctor(newDoctor);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DoctorDTO> doctors = service.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateData updateDoctor) {
        DoctorDTO updated = service.updateDoctor(id, updateDoctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id){
        service.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
