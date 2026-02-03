package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record DoctorUpdateData (
        @NotBlank(message = "O campo nome é obrigatório.")
        String name,
        @NotBlank(message = "O campo telefone é obrigatório.")
        String phone,
        @Valid AddressRegistrationData address
){}
