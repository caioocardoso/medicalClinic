package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.User;
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

    boolean existsByUser(User user);

    @Query("""
            SELECT count(d) FROM Doctor d
            WHERE d.active = TRUE
            AND NOT EXISTS (
                SELECT 1 FROM Appointment a
                WHERE a.doctor = d
                AND a.startDateTime < :end
                AND a.endDateTime > :start
            )
            """)
    Long countAvailableDoctors(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
            SELECT d FROM Doctor d
            WHERE d.active = TRUE
            AND NOT EXISTS (
                SELECT 1 FROM Appointment a
                WHERE a.doctor = d
                AND a.startDateTime < :end
                AND a.endDateTime > :start
            )
            """)
    Page<Doctor> findAvailableDoctors(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      Pageable pageable);
}
