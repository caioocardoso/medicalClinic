package org.medical.clinic.medicalclinic.services;

import org.medical.clinic.medicalclinic.DTO.RegisterDTO;
import org.medical.clinic.medicalclinic.models.Role;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.repositories.RoleRepository;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public User register (RegisterDTO registerDTO) {
        if(userRepository.findByLogin(registerDTO.login()) != null) {
            return null;
        }

        String hashedPassword = passwordEncoder.encode(registerDTO.password());
        User newUser = new User(registerDTO.login(), hashedPassword);

        Role userRole = roleRepository.findByRole("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        newUser.getRoles().add(userRole);

        return userRepository.save(newUser);
    }
}
