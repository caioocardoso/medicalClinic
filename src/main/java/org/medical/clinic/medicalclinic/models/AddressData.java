package org.medical.clinic.medicalclinic.models;

import jakarta.validation.constraints.NotBlank;

public record AddressData(
        @NotBlank String publicPlace,
        String number,
        String complement,
        @NotBlank String neighborhood,
        @NotBlank String city,
        @NotBlank String uf,
        @NotBlank String zipCode
) {}