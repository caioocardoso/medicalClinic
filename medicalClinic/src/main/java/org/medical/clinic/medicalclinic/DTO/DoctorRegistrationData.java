package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.medical.clinic.medicalclinic.models.Speciality;

public record DoctorRegistrationData(
        @NotBlank(message = "CRM is required")
        String crm,
        @NotNull(message = "Speciality is required")
        Speciality speciality
) {
}