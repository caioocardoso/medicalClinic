package org.medical.clinic.medicalclinic.services;

import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.*;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
        return user;
    }

    @Transactional
    public PatientDTO createNewPatient(PatientSignupRequest data) {
        if(userRepository.findByEmail(data.userData().getEmail()) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }
        User user = createUser(data.userData());
        user.getRoles().add(RoleType.ROLE_PATIENT);
        return patientService.createPatientProfile(user, data.patientData());
    }

    @Transactional
    public DoctorDTO createNewDoctor(DoctorSignupRequest data) {
        User user = createUser(data.userData());
        user.getRoles().add(RoleType.ROLE_DOCTOR);

        return doctorService.createDoctorProfile(user, data.doctorData());
    }

    @Transactional
    public User createUser(UserRegistrationData data) {
        if (userRepository.findByEmail(data.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }
        String hashedPassword = passwordEncoder.encode(data.getPassword());

        User newUser = new User(data, hashedPassword);
        return userRepository.save(newUser);
    }
}