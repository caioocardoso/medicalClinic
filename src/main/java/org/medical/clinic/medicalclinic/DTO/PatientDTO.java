package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Patient;
import org.medical.clinic.medicalclinic.models.Speciality;

public class PatientDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String cpf;

    public PatientDTO(){}
    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.name = patient.getName();
        this.email = patient.getEmail();
        this.cpf = patient.getCpf();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String mail) { this.email = mail; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
}
