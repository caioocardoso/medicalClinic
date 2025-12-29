package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Speciality;

public class DoctorDTO {
    private Long id;

    @NotBlank
    private String name;
    private String mail;
    private String phone;
    private String crm;
    private Speciality speciality;
    private Address address;

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
}
