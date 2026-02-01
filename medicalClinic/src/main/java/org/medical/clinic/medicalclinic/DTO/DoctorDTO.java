package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Speciality;

public class DoctorDTO {
    private Long id;
    private String name;
    private String email;
    private String crm;
    private Speciality speciality;

    public DoctorDTO(){}

    public DoctorDTO(Doctor doctor) {
        this.id = doctor.getId();
        this.crm = doctor.getCrm();
        this.speciality = doctor.getSpeciality();
        if (doctor.getUser() != null) {
            this.name = doctor.getUser().getName();
            this.email = doctor.getUser().getEmail();
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCrm() { return crm; }
    public Speciality getSpeciality() { return speciality; }
}