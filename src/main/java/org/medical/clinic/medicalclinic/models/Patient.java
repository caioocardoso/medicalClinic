package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.DTO.PatientRegistrationData;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    private String email;
    @Column(nullable = false)
    @NotBlank
    private String phone;
    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    private String cpf;
    @Embedded
    @NotNull
    @Valid
    private Address address;
    @Column(nullable = false)
    private boolean active = true;

    public Patient(){}

    public Patient(PatientRegistrationData data) {
        this.name = data.name();
        this.email = data.email();
        this.phone = data.phone();
        this.cpf = data.cpf();
        this.address = new Address(data.address());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
