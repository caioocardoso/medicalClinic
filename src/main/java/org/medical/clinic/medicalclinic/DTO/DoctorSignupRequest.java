package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;

public record DoctorSignupRequest(
        @Valid UserRegistrationData userData,
        @Valid DoctorRegistrationData doctorData
){}
