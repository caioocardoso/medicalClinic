package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.DTO.DoctorRequestDTO;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.DoctorRequest;
import org.medical.clinic.medicalclinic.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {
    Page<DoctorRequest> findAllByIsAcceptedFalse(Pageable pageable);
    boolean existsByUserAndFinishedFalse(User user);
}
