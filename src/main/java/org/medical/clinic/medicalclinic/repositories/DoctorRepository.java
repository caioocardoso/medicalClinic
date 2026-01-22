package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.models.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findAllByActiveTrue(Pageable pageable);

    @Query(value =
            "SELECT d.* FROM doctor d " +
                    "WHERE d.active = TRUE " +
                    "AND NOT EXISTS ( " +
                    "  SELECT 1 FROM appointment a " +
                    "  WHERE a.doctor_id = d.id " +
                    "    AND a.start_date_time < :end " +
                    "    AND a.end_date_time > :start " +
                    ") " +
                    "ORDER BY random() LIMIT 1",
            nativeQuery = true)
    Optional<Doctor> findRandomAvailableDoctor(@Param("start") LocalDateTime start,
                                               @Param("end") LocalDateTime end);
}
