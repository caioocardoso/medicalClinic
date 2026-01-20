package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.models.Appointment;
import org.medical.clinic.medicalclinic.models.AppointmentStatus;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("select count(a) > 0 from Appointment a " +
            "where a.doctor = :doctor " +
            "and a.status not in :ignoredStatuses " +
            "and a.startDateTime < :end " +
            "and a.endDateTime > :start")
    boolean existsDoctorConflict(@Param("doctor") Doctor doctor,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end,
                                 @Param("ignoredStatuses") Collection<AppointmentStatus> ignoredStatuses);

    @Query("select count(a) > 0 from Appointment a where a.patient = :patient and a.startDateTime >= :dayStart and a.endDateTime < :dayEnd")
    boolean existsPatientAppointmentOnDay(@Param("patient") Patient patient,
                                          @Param("dayStart") LocalDateTime dayStart,
                                          @Param("dayEnd") LocalDateTime dayEnd);
}
