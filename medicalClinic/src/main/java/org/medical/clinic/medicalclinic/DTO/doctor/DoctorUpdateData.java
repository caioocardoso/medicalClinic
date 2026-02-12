package org.medical.clinic.medicalclinic.DTO.doctor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.DTO.AddressRegistrationData;

public record DoctorUpdateData (
        @NotBlank(message = "O campo nome é obrigatório.")
        String name,
        @NotBlank(message = "O campo telefone é obrigatório.")
        String phone,
        @Valid AddressRegistrationData address
){}
