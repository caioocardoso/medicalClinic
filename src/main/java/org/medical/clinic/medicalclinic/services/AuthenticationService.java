package org.medical.clinic.medicalclinic.services;

import org.medical.clinic.medicalclinic.DTO.RegisterDTO;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Transactional
    public User register(RegisterDTO data, RoleType roleType) {
        if(userRepository.findByEmail(data.email()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data, hashedPassword);

        RoleType targetRole = (roleType != null) ? roleType : RoleType.ROLE_PATIENT;
        newUser.getRoles().add(targetRole);

        User savedUser = userRepository.save(newUser);

        try {
            if (targetRole == RoleType.ROLE_PATIENT) {
                if(data.cpf() == null) throw new IllegalArgumentException("CPF is required");
                Patient patient = new Patient();
                patient.setUser(newUser);
                patient.setCpf(data.cpf());
                patientRepository.save(patient);
            } else if (targetRole == RoleType.ROLE_DOCTOR) {
                if (data.crm() == null || data.speciality() == null)
                    throw new IllegalArgumentException("CRM and Speciality is required");
                Doctor doctor = new Doctor();
                doctor.setUser(newUser);
                doctor.setCrm(data.crm());
                doctor.setSpeciality(data.speciality());
                doctorRepository.save(doctor);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar perfil vinculado: " + e.getMessage());
        }

        return savedUser;
    }
}