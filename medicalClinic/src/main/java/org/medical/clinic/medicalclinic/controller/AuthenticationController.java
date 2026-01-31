package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.AuthenticationService;
import org.medical.clinic.medicalclinic.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(token));
    }

    @PostMapping("/register/patient")
    public ResponseEntity<PatientDTO> registerPatient(@RequestBody @Valid PatientSignupRequest data) {
        PatientDTO savedUser = authenticationService.createNewPatient(data);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<DoctorDTO> registerDoctor(@RequestBody @Valid DoctorSignupRequest data) {
        DoctorDTO savedUser = authenticationService.createNewDoctor(data);
        return ResponseEntity.ok(savedUser);
    }
}
