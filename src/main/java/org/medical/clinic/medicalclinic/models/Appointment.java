package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.*;
import org.medical.clinic.medicalclinic.DTO.AppointmentRequest;

import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(optional = false)
    private Doctor doctor;
    @ManyToOne
    private Patient patient;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private int durationInMinutes = 60;
    @Column(nullable = false)
    private Enum status = AppointmentStatus.SCHEDULED;

    public Appointment() {
    }

    public Appointment(Doctor doctor, Patient patient, LocalDateTime startDateTime) {
        this.doctor = doctor;
        this.patient = patient;
        this.startDateTime = startDateTime;
        this.endDateTime = startDateTime.plusMinutes(durationInMinutes);
    }

    public Appointment(AppointmentRequest appointmentRequest) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
