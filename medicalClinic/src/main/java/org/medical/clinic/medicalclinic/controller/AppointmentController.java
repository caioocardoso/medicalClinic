package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.AppointmentCancellationRequest;
import org.medical.clinic.medicalclinic.DTO.AppointmentDTO;
import org.medical.clinic.medicalclinic.DTO.AppointmentRequest;
import org.medical.clinic.medicalclinic.clients.EmailClient;
import org.medical.clinic.medicalclinic.clients.EmailDto;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta")
public class AppointmentController {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService service;
    @Autowired
    private EmailClient emailClient;

    @PostMapping
    public ResponseEntity<AppointmentDTO> schedule(@AuthenticationPrincipal User user, @RequestBody @Valid AppointmentRequest newAppointment) {
        AppointmentDTO saved = service.schedule(newAppointment);

        // Tenta enviar email mas não falha o agendamento se o serviço de email estiver indisponível
        try {
            EmailDto email = new EmailDto(
                    user.getEmail(),
                    "Consulta Agendada com Sucesso!",
                    "Olá " + user.getName() + ", sua consulta foi agendada com sucesso para o dia " +
                            saved.dateTime() + "."
            );
            emailClient.sendEmail(email);
            logger.info("Email de confirmação de agendamento enviado para: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de confirmação para {}: {}", user.getEmail(), e.getMessage());
            // Continua mesmo assim - consulta foi agendada com sucesso
        }

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

    @GetMapping("/paciente")
    public ResponseEntity<List<AppointmentDTO>> getMyAppointments(@AuthenticationPrincipal User user) {
        List<AppointmentDTO> appointments = service.getAppointmentsByUser(user);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/paciente/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable Long patientId) {
        List<AppointmentDTO> appointments = service.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/medico/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<AppointmentDTO> appointments = service.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping
    public ResponseEntity<AppointmentDTO> cancel(@AuthenticationPrincipal User user, @RequestBody @Valid AppointmentCancellationRequest cancellation) {
        AppointmentDTO appointmentCancelled = service.cancel(cancellation);

        // Tenta enviar email mas não falha o cancelamento se o serviço de email estiver indisponível
        try {
            EmailDto email = new EmailDto(
                    user.getEmail(),
                    "Consulta Cancelada com Sucesso!",
                    "Olá, sua consulta marcada para o dia " + appointmentCancelled.dateTime() + " foi cancelada com sucesso."
            );
            emailClient.sendEmail(email);
            logger.info("Email de confirmação de cancelamento enviado para: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de cancelamento para {}: {}", user.getEmail(), e.getMessage());
            // Continua mesmo assim - consulta foi cancelada com sucesso
        }

        return ResponseEntity.ok(appointmentCancelled);
    }
}
