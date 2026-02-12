package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.doctor.DoctorRegistrationData;

import java.time.LocalDateTime;

@Entity
public class DoctorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    @Valid
    private User user;

    @Embedded
    @Valid DoctorRegistrationData doctorRegistrationData;

    private boolean isAccepted = false;
    private boolean finished = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public DoctorRequest() {}

    public DoctorRequest(User user, DoctorRegistrationData doctorRegistrationData) {
        this.user = user;
        this.doctorRegistrationData = doctorRegistrationData;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public DoctorRegistrationData getDoctorRegistrationData() {return doctorRegistrationData;}
    public void setDoctorRegistrationData(DoctorRegistrationData doctorRegistrationData) {this.doctorRegistrationData = doctorRegistrationData;}
    public boolean isAccepted() {return isAccepted;}
    public void setAccepted(boolean accepted) {isAccepted = accepted;}
    public boolean isFinished() {return finished;}
    public void setFinished(boolean finished) {this.finished = finished;}
}
