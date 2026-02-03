package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotNull;

public record ApprovalDoctorRequest (
    @NotNull(message = "O ID é obrigatório")
    Long id,
    @NotNull(message = "O status de aprovação é obrigatório")
    Boolean isApproved
){}