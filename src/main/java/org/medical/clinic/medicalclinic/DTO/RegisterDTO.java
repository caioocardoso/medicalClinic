package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.models.RoleType;
import org.medical.clinic.medicalclinic.models.Speciality;

public record RegisterDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String phone,
        @Valid AddressRegistrationData address,
        String cpf,
        String crm,
        Speciality speciality,
        RoleType role
) {}