package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;

public record ApprovalDoctorRequest (
    @NotNull(message = "ID is required")
    Long id,
    @NotNull(message = "Approval status is required")
    Boolean isApproved
){}