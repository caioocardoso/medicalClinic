package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.medical.clinic.medicalclinic.models.Speciality;

public record DoctorRegistrationData(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+?[0-9\\s()\\-]{8,20}$", message = "Invalid phone")
        String phone,
        @NotBlank(message = "CRM is required")
        String crm,
        @NotNull(message = "Speciality is required")
        Speciality speciality,
        @NotNull(message = "Address is required")
        @Valid
        AddressRegistrationData address
) {
}