package org.medical.clinic.medicalclinic.DTO.patient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.DTO.AddressRegistrationData;

public record PatientUpdateData(
        @NotBlank(message = "O campo nome é obrigatório.")
        String name,
        @NotBlank(message = "O campo telefone é obrigatório.")
        String phone,
        @Valid AddressRegistrationData address
){}
