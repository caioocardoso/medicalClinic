package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.AppointmentDTO;
import org.medical.clinic.medicalclinic.DTO.AppointmentRequest;
import org.medical.clinic.medicalclinic.models.Appointment;
import org.medical.clinic.medicalclinic.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consulta")
public class AppointmentController {
    private AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> schedule(@RequestBody @Valid AppointmentRequest newAppointment) {
        AppointmentDTO saved = service.schedule(newAppointment);
        return ResponseEntity.status(201).body(saved);
    }
}
