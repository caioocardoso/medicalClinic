package org.medical.clinic.medicalclinic.DTO.doctor;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.UserRegistrationData;

public record DoctorSignupRequest(
        @Valid UserRegistrationData userData,
        @Valid DoctorRegistrationData doctorData
){}
