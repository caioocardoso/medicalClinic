package org.medical.clinic.medicalclinic.DTO.doctor;

import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.RoleType;
import org.medical.clinic.medicalclinic.models.Speciality;

import java.util.Set;

public class DoctorProfileDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Address address;
    private String crm;
    private Speciality speciality;
    private boolean active;
    private Set<RoleType> roles;

    public DoctorProfileDTO(Doctor doctor) {
        if (doctor.getUser() != null) {
            this.userId = doctor.getUser().getId();
            this.name = doctor.getUser().getName();
            this.email = doctor.getUser().getEmail();
            this.phone = doctor.getUser().getPhone();
            this.address = doctor.getUser().getAddress();
            this.roles = doctor.getUser().getRoles();
        }
        this.id = doctor.getId();
        this.crm = doctor.getCrm();
        this.speciality = doctor.getSpeciality();
        this.active = doctor.isActive();
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

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
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

