package org.medical.clinic.medicalclinic.DTO;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.medical.clinic.medicalclinic.models.Speciality;

@Embeddable
public record DoctorRegistrationData(
        @NotBlank(message = "CRM is required")
        String crm,
        @NotNull(message = "Speciality is required")
        @Enumerated(EnumType.STRING)
        Speciality speciality
) {
        public DoctorRegistrationData(DoctorRegistrationData doctorRegistrationData) {
                this(doctorRegistrationData.crm(), doctorRegistrationData.speciality());
        }
}