package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;

public record DoctorUpdateData (
        String name,
        String phone,
        @Valid AddressRegistrationData address
){}
