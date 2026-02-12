package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.models.DoctorRequest;
import org.medical.clinic.medicalclinic.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {
    Page<DoctorRequest> findAllByIsAcceptedFalse(Pageable pageable);
    boolean existsByUserAndFinishedFalse(User user);
}
