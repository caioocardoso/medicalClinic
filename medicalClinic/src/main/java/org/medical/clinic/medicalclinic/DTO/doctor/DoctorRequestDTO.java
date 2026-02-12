package org.medical.clinic.medicalclinic.DTO.doctor;

import org.medical.clinic.medicalclinic.DTO.UserDTO;
import org.medical.clinic.medicalclinic.models.DoctorRequest;

import java.time.LocalDateTime;

public record DoctorRequestDTO(
        Long id,
    UserDTO userDTO,
    DoctorRegistrationData doctorRegistrationData,
    boolean isAccepted,
        LocalDateTime createdAt,
        boolean isFinished
) {
    public DoctorRequestDTO(DoctorRequest doctorRequest) {
        this(
            doctorRequest.getId(),
            new UserDTO(doctorRequest.getUser()),
            new DoctorRegistrationData(doctorRequest.getDoctorRegistrationData()),
                doctorRequest.isAccepted(),
                doctorRequest.getCreatedAt(),
                doctorRequest.isFinished()
        );
    }
}
