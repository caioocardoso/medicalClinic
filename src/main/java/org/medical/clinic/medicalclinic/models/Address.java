package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Address {
    @NotBlank
    private String publicPlace;
    @NotBlank
    private String number;
    private String complement;
    @NotBlank
    private String neighborhood;
    @NotBlank
    private String city;
    @NotBlank
    private String uf;
    @NotBlank
    private String zipCode;

    public Address(@NotNull @Valid Address address) {
        this.publicPlace = address.publicPlace;
        this.number = address.number;
        this.complement = address.complement;
        this.neighborhood = address.neighborhood;
        this.city = address.city;
        this.uf = address.uf;
        this.zipCode = address.zipCode;
    }
}
