package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull
        Long patientId,
        Long doctorId, // optional
        @NotNull
        LocalDateTime dateTime
) {}