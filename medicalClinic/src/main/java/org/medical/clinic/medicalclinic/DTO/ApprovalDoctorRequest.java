package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;

public record ApprovalDoctorRequest (
    @NotNull Long id, @NotNull Boolean isApproved
){}