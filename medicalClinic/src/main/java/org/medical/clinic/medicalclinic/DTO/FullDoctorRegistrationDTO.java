package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record FullDoctorRegistrationDTO(
    @NotNull @Valid UserRegistrationData userData,
    @NotNull @Valid DoctorRegistrationData doctorData
) {}
