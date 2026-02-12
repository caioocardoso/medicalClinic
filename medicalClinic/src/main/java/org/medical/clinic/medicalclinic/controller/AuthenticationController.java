package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.DTO.patient.PatientDTO;
import org.medical.clinic.medicalclinic.DTO.patient.PatientSignupRequest;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.AuthenticationService;
import org.medical.clinic.medicalclinic.services.TokenService;
import org.medical.clinic.medicalclinic.clients.EmailClient;
import org.medical.clinic.medicalclinic.clients.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private EmailClient emailClient;

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

        try {
            EmailDto email = new EmailDto(data.userData().getEmail(), "Cadastro realizado com sucesso!", "Olá " + data.userData().getName() + ", seu cadastro foi realizado com sucesso no nosso sistema.!");
            emailClient.sendEmail(email);
            logger.info("Email de confirmação de cadastro enviado para: {}", data.userData().getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de cadastro para {}: {}", data.userData().getEmail(), e.getMessage());
        }

        return ResponseEntity.ok(savedUser);
    }
}
