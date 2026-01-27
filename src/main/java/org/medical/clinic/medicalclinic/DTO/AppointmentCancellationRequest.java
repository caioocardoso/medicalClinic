package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.models.CancellationReason;

public record AppointmentCancellationRequest(
        @NotNull
        Long appointmentId,
        @NotNull
        CancellationReason reason
) {
}
