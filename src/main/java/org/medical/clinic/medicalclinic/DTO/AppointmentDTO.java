package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.Appointment;

import java.time.LocalDateTime;

public record AppointmentDTO(
        Long id,
        Long patientId,
        Long doctorId,
        LocalDateTime dateTime
) {
    public AppointmentDTO(Appointment appointment) {
        this(appointment.getId(), appointment.getPatient().getId(), appointment.getDoctor().getId(), appointment.getStartDateTime());
    }
}