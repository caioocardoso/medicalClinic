package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.Patient;

public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String phone;

    public PatientDTO(Patient patient) {
        this.id = patient.getId();
        this.cpf = patient.getCpf();
        if (patient.getUser() != null) {
            this.name = patient.getUser().getName();
            this.email = patient.getUser().getEmail();
            this.phone = patient.getUser().getPhone();
        }
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getPhone() { return phone; }
}