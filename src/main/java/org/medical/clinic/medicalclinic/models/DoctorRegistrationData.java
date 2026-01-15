package org.medical.clinic.medicalclinic.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DoctorRegistrationData(
        @NotBlank String name,
        @NotBlank @Email String mail,
        String phone,
        @NotBlank String crm,
        @NotNull Speciality speciality,
        @NotNull @Valid AddressData address
) {
}