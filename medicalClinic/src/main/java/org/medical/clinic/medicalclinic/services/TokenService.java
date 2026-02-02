package org.medical.clinic.medicalclinic.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.medical.clinic.medicalclinic.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer("medical-clinic")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate());

            // Add roles
            List<String> roles = user.getRoles().stream()
                    .map(Enum::name)
                    .collect(Collectors.toList());
            builder.withClaim("roles", roles);

            // Add IDs based on role
            if (roles.contains("ROLE_PATIENT")) {
                patientRepository.findByUser(user).ifPresent(p -> builder.withClaim("patientId", p.getId()));
            }
            if (roles.contains("ROLE_DOCTOR")) {
                doctorRepository.findByUser(user).ifPresent(d -> builder.withClaim("doctorId", d.getId()));
            }
            
            return builder.sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String validateToken(String token) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("medical-clinic")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "Invalid token";
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
