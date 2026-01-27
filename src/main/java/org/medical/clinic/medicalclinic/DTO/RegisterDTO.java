package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank String login,
        @NotBlank String password) {
}