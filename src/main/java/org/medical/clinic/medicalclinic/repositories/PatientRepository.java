package org.medical.clinic.medicalclinic.repositories;

import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findAllByActiveTrue(Pageable pageable);
}
