package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Speciality;

public class DoctorDTO {
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String mail;
    private String phone;
    @NotBlank
    private String crm;
    @NotBlank
    private Speciality speciality;
    private Address address;

    public DoctorDTO(){}
    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.mail = doctor.getMail();
        this.phone = doctor.getPhone();
        this.crm = doctor.getCrm();
        this.speciality = doctor.getSpeciality();
        this.address = doctor.getAddress();
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
}
