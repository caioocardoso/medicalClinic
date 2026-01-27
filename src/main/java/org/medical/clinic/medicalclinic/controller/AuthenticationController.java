package org.medical.clinic.medicalclinic.controller;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.LoginDTO;
import org.medical.clinic.medicalclinic.DTO.RegisterDTO;
import org.medical.clinic.medicalclinic.DTO.TokenDTO;
import org.medical.clinic.medicalclinic.DTO.UserDTO;
import org.medical.clinic.medicalclinic.models.RoleType;
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
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterDTO data) {
        User savedUser = authenticationService.register(data, RoleType.ROLE_PATIENT);
        if (savedUser == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new UserDTO(savedUser));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody @Valid RegisterDTO data) {
        if (data.role() == null) {
            return ResponseEntity.badRequest().build();
        }

        User savedUser = authenticationService.register(data, data.role());

        if(savedUser == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new UserDTO(savedUser));
    }
}
