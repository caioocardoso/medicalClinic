package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.AppointmentCancellationRequest;
import org.medical.clinic.medicalclinic.DTO.AppointmentDTO;
import org.medical.clinic.medicalclinic.DTO.AppointmentRequest;
import org.medical.clinic.medicalclinic.models.Appointment;
import org.medical.clinic.medicalclinic.services.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointment(
            @PageableDefault(size = 10, sort = "startDateTime", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AppointmentDTO> appointments = service.getAllAppointments(pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = service.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/paciente/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable Long patientId) {
        List<AppointmentDTO> appointments = service.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping
    public ResponseEntity cancel(@RequestBody @Valid AppointmentCancellationRequest cancellation) {
        service.cancel(cancellation);
        return ResponseEntity.noContent().build();
    }
}
