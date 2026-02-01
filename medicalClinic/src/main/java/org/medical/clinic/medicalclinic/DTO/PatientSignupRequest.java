package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;

public record PatientSignupRequest(
        @Valid UserRegistrationData userData,
        @Valid PatientRegistrationData patientData
) {
}
