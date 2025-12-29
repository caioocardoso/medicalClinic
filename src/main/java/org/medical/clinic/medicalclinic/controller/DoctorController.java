package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<DoctorDTO> newDoctor(@Valid @RequestBody DoctorDTO newDoctor) {
        DoctorDTO saved = service.newDoctor(newDoctor);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = service.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}
