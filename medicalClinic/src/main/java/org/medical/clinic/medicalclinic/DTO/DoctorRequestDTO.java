package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.DoctorRequest;

public record DoctorRequestDTO(
        Long id,
    UserDTO userDTO,
    DoctorRegistrationData doctorRegistrationData,
    boolean isAccepted
) {
    public DoctorRequestDTO(DoctorRequest doctorRequest) {
        this(
            doctorRequest.getId(),
            new UserDTO(doctorRequest.getUser()),
            new DoctorRegistrationData(doctorRequest.getDoctorRegistrationData()),
                false
        );
    }
}
