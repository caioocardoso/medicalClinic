package org.medical.clinic.medicalclinic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        .requestMatchers(HttpMethod.POST, "/paciente/perfil").authenticated()
                        .requestMatchers(HttpMethod.POST, "/auth/register/patient").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/register/doctor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/medico/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.POST, "/medico/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/medico/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/medico/**").hasAnyRole("DOCTOR")

                        .requestMatchers(HttpMethod.GET, "/paciente").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/paciente/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/paciente/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/consulta").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.DELETE, "/consulta").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/consulta/paciente/**").hasRole("PATIENT")
                        .requestMatchers(HttpMethod.GET, "/consulta").hasRole("DOCTOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("MASTER").implies("ADMIN")
                .role("ADMIN").implies("DOCTOR")
                .role("DOCTOR").implies("PATIENT")
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
