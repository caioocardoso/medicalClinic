package org.medical.clinic.medicalclinic.DTO.appointment;

import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.models.CancellationReason;

public record AppointmentCancellationRequest(
        @NotNull(message = "O ID da consulta é obrigatório")
        Long appointmentId,
        @NotNull(message = "O motivo do cancelamento é obrigatório")
        CancellationReason reason
) {
}
