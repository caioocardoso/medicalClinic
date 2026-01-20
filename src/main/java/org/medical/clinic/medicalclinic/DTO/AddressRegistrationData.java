package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRegistrationData(
        @NotBlank(message = "Street is required")
        String publicPlace,
        @NotNull(message = "Number is required")
        Integer number,
        String complement,
        @NotBlank(message = "Neighborhood is required")
        String neighborhood,
        @NotBlank(message = "City is required")
        String city,
        @NotBlank(message = "State (UF) is required")
        @Size(min = 2, max = 2, message = "State (UF) must have 2 letters")
        String uf,
        @NotBlank(message = "CEP is required")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Invalid CEP")
        String zipCode
) {}