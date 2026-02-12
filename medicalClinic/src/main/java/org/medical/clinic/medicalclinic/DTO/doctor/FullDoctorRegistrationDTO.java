package org.medical.clinic.medicalclinic.DTO.doctor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.DTO.UserRegistrationData;

public record FullDoctorRegistrationDTO(
    @NotNull(message = "Os dados do usuário são obrigatórios")
    @Valid
    UserRegistrationData userData,
    @NotNull(message = "Os dados do médico são obrigatórios")
    @Valid
    DoctorRegistrationData doctorData
) {}
