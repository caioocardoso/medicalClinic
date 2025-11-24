package org.medical.clinic.medicalclinic.DTO;

import jakarta.persistence.*;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.models.Speciality;

public class DoctorDTO {
    private String name;
    private String mail;
    private String phone;
    private String crm;
    private Speciality speciality;
    private Address address;
}
