package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;
import org.medical.clinic.medicalclinic.models.RoleType;

public record RegisterDTO(
        @NotBlank String login,
        @NotBlank String password,
        RoleType role
) {
}