package org.medical.clinic.medicalclinic.models;

import jakarta.validation.Valid;

public record DoctorUpdateData (
        String name,
        String phone,
        @Valid AddressData address
){}
