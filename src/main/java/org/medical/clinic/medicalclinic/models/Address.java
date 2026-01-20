package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.DTO.AddressRegistrationData;

@Embeddable
public class Address {
    @Column(name = "public_place", nullable = false)
    @NotBlank
    private String publicPlace;
    @Column(name = "number", nullable = false)
    @NotNull
    private Integer number;
    @Column(name = "complement")
    private String complement;
    @Column(name = "neighborhood", nullable = false)
    @NotBlank
    private String neighborhood;
    @Column(name = "city", nullable = false)
    @NotBlank
    private String city;
    @Column(name = "uf", nullable = false, length = 2)
    @NotBlank
    private String uf;

    @Column(name = "zip_code", nullable = false)
    @NotBlank
    private String zipCode;

    public Address() {}

    public Address(Address address) {
        this.publicPlace = address.publicPlace;
        this.number = address.number;
        this.complement = address.complement;
        this.neighborhood = address.neighborhood;
        this.city = address.city;
        this.uf = address.uf;
        this.zipCode = address.zipCode;
    }

    public Address(@NotNull @Valid AddressRegistrationData data) {
        this.publicPlace = data.publicPlace();
        this.number = data.number();
        this.complement = data.complement();
        this.neighborhood = data.neighborhood();
        this.city = data.city();
        this.uf = data.uf();
        this.zipCode = data.zipCode();
    }

    public String getPublicPlace() { return publicPlace; }
    public Integer getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getUf() { return uf; }
    public String getZipCode() { return zipCode; }
}