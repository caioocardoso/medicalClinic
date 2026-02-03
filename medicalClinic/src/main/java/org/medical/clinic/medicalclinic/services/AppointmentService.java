package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.AppointmentRepository;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora são obrigatórios");
        if(dateTime.isBefore(LocalDateTime.now().plusMinutes(30)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consultas devem ser agendadas com pelo menos 30 minutos de antecedência");
        if(dateTime.getDayOfWeek() == DayOfWeek.SUNDAY)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clínica fechada aos domingos");
        LocalTime localTime = dateTime.toLocalTime();
        if(localTime.isBefore(CLINIC_OPENING_TIME) || localTime.isAfter(CLINIC_CLOSING_TIME))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consultas devem ser agendadas entre 07:00 e 19:00");

        Patient patient = patientRepository.findById(appointment.patientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
        if(!patient.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar consulta para paciente inativo");

        LocalDateTime dayStart = dateTime.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);
        if (appointmentRepository.existsPatientAppointmentOnDay(patient, dayStart, dayEnd))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente já possui consulta agendada no dia");

        LocalDateTime start = dateTime;
        LocalDateTime end = start.plusHours(1);

        Doctor chosenDoctor;
        if (appointment.doctorId() != null) {
            chosenDoctor = doctorRepository.findById(appointment.doctorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));
            if (!chosenDoctor.isActive())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar consulta para médico inativo");
            var ignoreStatus = List.of(AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);

            boolean conflict = appointmentRepository.existsDoctorConflict(chosenDoctor, start, end, ignoreStatus);
            if (conflict)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Médico não está disponível no horário solicitado");
        } else {
            Long totalAvailable = doctorRepository.countAvailableDoctors(start, end);
            if (totalAvailable == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há médicos disponíveis no horário solicitado");
            }
            int randomIndex = random.nextInt(totalAvailable.intValue());
            Page<Doctor> doctorPage = doctorRepository.findAvailableDoctors(start, end, PageRequest.of(randomIndex, 1));

            chosenDoctor = doctorPage.getContent().get(0);
        }

        Appointment newAppointment = new Appointment(chosenDoctor, patient, dateTime);
        Appointment saved = appointmentRepository.save(newAppointment);
        return new AppointmentDTO(saved);
    }

    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(AppointmentDTO::new);
    }

    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));
        return new AppointmentDTO(appointment);
    }

    public List<AppointmentDTO> getAppointmentsByUser(User user) {
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não está identificado como paciente"));

        if (!patient.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil do paciente está inativo");
        }

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));

        if(!patient.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente inativo não possui consultas");

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médico não encontrado"));

        if(!doctor.isActive())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Médico inativo não possui consultas");

        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);
        return appointments.stream().map(AppointmentDTO::new).toList();
    }

    public AppointmentDTO cancel(AppointmentCancellationRequest cancellation) {
        if (cancellation.appointmentId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID da consulta é obrigatório");
        }

        Appointment appointment = appointmentRepository.findById(cancellation.appointmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consulta não encontrada"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consulta já está cancelada");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentTime = appointment.getStartDateTime();

        if (now.plusHours(24).isAfter(appointmentTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consultas só podem ser canceladas com pelo menos 24 horas de antecedência");
        }

        appointment.cancel(cancellation.reason());
        return new AppointmentDTO(appointmentRepository.save(appointment));
    }
}
