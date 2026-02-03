package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientUpdateData(
        @NotBlank(message = "O campo nome é obrigatório.")
        String name,
        @NotBlank(message = "O campo telefone é obrigatório.")
        String phone,
        @Valid AddressRegistrationData address
){}
