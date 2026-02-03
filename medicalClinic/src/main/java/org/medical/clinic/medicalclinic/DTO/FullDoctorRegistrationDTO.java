package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FullDoctorRegistrationDTO(
    @NotNull(message = "Os dados do usuário são obrigatórios")
    @Valid
    UserRegistrationData userData,
    @NotNull(message = "Os dados do médico são obrigatórios")
    @Valid
    DoctorRegistrationData doctorData
) {}
