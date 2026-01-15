package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true, updatable = false)
    private String mail;
    private String phone;
    @Column(nullable = false, unique = true, updatable = false)
    private String crm;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Speciality speciality;
    @Embedded
    private Address address;
    @Column(nullable = false)
    private boolean active = true;

    public Doctor(){}

    public Doctor(DoctorRegistrationData data) {
        this.name = data.name();
        this.mail = data.mail();
        this.phone = data.phone();
        this.crm = data.crm();
        this.speciality = data.speciality();
        this.address = new Address(data.address());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getCrm() { return crm; }
    public void setCrm(String crm) { this.crm = crm; }
    public Speciality getSpeciality() { return speciality; }
    public void setSpeciality(Speciality speciality) { this.speciality = speciality; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
