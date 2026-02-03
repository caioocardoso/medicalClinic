package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientRegistrationData(
        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(regexp = "^(?=(?:.*\\d){11}$)[\\d.-]+$", message = "O CPF deve conter exatamente 11 dígitos")
        String cpf
) {
}