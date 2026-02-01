package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;

public record PatientUpdateData(
        String name,
        String phone,
        @Valid AddressRegistrationData address
){}
