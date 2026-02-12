package org.medical.clinic.medicalclinic.DTO.patient;

import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.models.RoleType;

import java.util.Set;

public class PatientProfileDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Address address;
    private String cpf;
    private boolean active;
    private Set<RoleType> roles;

    public PatientProfileDTO(Patient patient) {
        if (patient.getUser() != null) {
            this.userId = patient.getUser().getId();
            this.name = patient.getUser().getName();
            this.email = patient.getUser().getEmail();
            this.phone = patient.getUser().getPhone();
            this.address = patient.getUser().getAddress();
            this.roles = patient.getUser().getRoles();
        }
        this.id = patient.getId();
        this.cpf = patient.getCpf();
        this.active = patient.isActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }
}

