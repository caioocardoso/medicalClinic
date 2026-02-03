package org.medical.clinic.medicalclinic.DTO;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.medical.clinic.medicalclinic.models.Speciality;

@Embeddable
public record DoctorRegistrationData(
        @NotBlank(message = "O CRM é obrigatório")
        String crm,
        @NotNull(message = "A especialidade é obrigatória")
        @Enumerated(EnumType.STRING)
        Speciality speciality
) {
        public DoctorRegistrationData(DoctorRegistrationData doctorRegistrationData) {
                this(doctorRegistrationData.crm(), doctorRegistrationData.speciality());
        }
}