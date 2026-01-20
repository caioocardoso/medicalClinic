package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.AppointmentRepository;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final Random random = new Random();

    private static LocalTime CLINIC_OPENING_TIME = LocalTime.of(7, 0);
    private static LocalTime CLINIC_CLOSING_TIME = LocalTime.of(19, 0);

    public AppointmentService(AppointmentRepository repository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public AppointmentDTO schedule(@Valid AppointmentRequest appointment) {
        LocalDateTime dateTime = appointment.dateTime();
        if(dateTime == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dateTime is required");
        if(dateTime.isBefore(LocalDateTime.now().plusMinutes(30)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments must be scheduled at least 30 minutes in advance");
        if(dateTime.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clinic closed on Sundays");
        LocalTime localTime = dateTime.toLocalTime();
        if(localTime.isBefore(CLINIC_OPENING_TIME) || localTime.isAfter(CLINIC_CLOSING_TIME))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointments must be between 07:00 and 19:00");

        Patient patient = patientRepository.findById(appointment.patientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        if(!patient.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot schedule appointment for inactive patient");

        LocalDateTime dayStart = dateTime.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);
        if (appointmentRepository.existsPatientAppointmentOnDay(patient, dayStart, dayEnd))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Patient already appointment on day");

        LocalDateTime start = dateTime;
        LocalDateTime end = start.plusHours(1);

        Doctor chosenDoctor;
        if (appointment.doctorId() != null) {
            chosenDoctor = doctorRepository.findById(appointment.doctorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));
            if (!chosenDoctor.isActive())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot schedule appointment for inactive doctor");
            var ignoreStatus = List.of(AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);

            boolean conflict = appointmentRepository.existsDoctorConflict(chosenDoctor, start, end, ignoreStatus);
            if (conflict)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is not available at the requested time");
        } else {
            chosenDoctor = doctorRepository.findRandomAvailableDoctor(start, end)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available doctors at the requested time"));
        }

        Appointment newAppointment = new Appointment(chosenDoctor, patient, dateTime);
        Appointment saved = appointmentRepository.save(newAppointment);
        return new AppointmentDTO(saved);
    }
}
