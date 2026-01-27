package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.DTO.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.DTO.DoctorUpdateData;
import org.medical.clinic.medicalclinic.DTO.PatientDTO;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medico")
@Validated
public class DoctorController {
    @Autowired
    DoctorService service;

    @GetMapping
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(
            @PageableDefault(size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DoctorDTO> doctors = service.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateData updateDoctor) {
        DoctorDTO updated = service.updateDoctor(id, updateDoctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable Long id){
        DoctorDTO deleted = service.deleteDoctor(id);
        return ResponseEntity.ok(deleted);
    }
}