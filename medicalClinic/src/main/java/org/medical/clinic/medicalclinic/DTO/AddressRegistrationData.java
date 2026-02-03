package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRegistrationData(
        @NotBlank(message = "O logradouro é obrigatório")
        String publicPlace,
        @NotNull(message = "O número é obrigatório")
        Integer number,
        String complement,
        @NotBlank(message = "O bairro é obrigatório")
        String neighborhood,
        @NotBlank(message = "A cidade é obrigatória")
        String city,
        @NotBlank(message = "O estado (UF) é obrigatório")
        @Size(min = 2, max = 2, message = "O estado (UF) deve ter 2 letras")
        String uf,
        @NotBlank(message = "O CEP é obrigatório")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido. Formato esperado: 12345-678 ou 12345678")
        String zipCode
) {}