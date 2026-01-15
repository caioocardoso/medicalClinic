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

    public Address() {}

    public Address(@NotNull @Valid Address address) {
        this.publicPlace = address.publicPlace;
        this.number = address.number;
        this.complement = address.complement;
        this.neighborhood = address.neighborhood;
        this.city = address.city;
        this.uf = address.uf;
        this.zipCode = address.zipCode;
    }

    public Address(@NotNull @Valid AddressData data) {
        this.publicPlace = data.publicPlace();
        this.number = data.number();
        this.complement = data.complement();
        this.neighborhood = data.neighborhood();
        this.city = data.city();
        this.uf = data.uf();
        this.zipCode = data.zipCode();
    }

    public String getPublicPlace() { return publicPlace; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getUf() { return uf; }
    public String getZipCode() { return zipCode; }
}
