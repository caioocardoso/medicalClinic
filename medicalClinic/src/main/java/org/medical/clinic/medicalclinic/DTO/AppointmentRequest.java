package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull(message = "O ID do paciente é obrigatório")
        Long patientId,
        Long doctorId, // opcional
        @NotNull(message = "A data e hora da consulta são obrigatórias")
        LocalDateTime dateTime
) {}