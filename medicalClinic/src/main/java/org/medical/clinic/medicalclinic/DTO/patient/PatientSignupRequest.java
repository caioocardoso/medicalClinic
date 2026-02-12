package org.medical.clinic.medicalclinic.DTO.patient;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.UserRegistrationData;

public record PatientSignupRequest(
        @Valid UserRegistrationData userData,
        @Valid PatientRegistrationData patientData
) {
}
