package org.medical.clinic.medicalclinic.config;

import org.medical.clinic.medicalclinic.DTO.AddressRegistrationData;
import org.medical.clinic.medicalclinic.models.RoleType;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.models.Address;
import org.medical.clinic.medicalclinic.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@admin.com") == null) {

            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@admin.com");
            admin.setPhone("11999999999");
            admin.setPassword(passwordEncoder.encode("admin"));

            Address address = new Address();
            address.setPublicPlace("Rua Admin");
            address.setNumber(1);
            address.setNeighborhood("Centro");
            address.setCity("São Paulo");
            address.setUf("SP");
            address.setZipCode("01001-000");

            admin.getRoles().add(RoleType.ROLE_ADMIN);
            admin.setAddress(address);

            userRepository.save(admin);

            System.out.println("---------------------------------");
            System.out.println(" USUÁRIO ADMIN CRIADO COM SUCESSO");
            System.out.println(" Email: admin@admin.com");
            System.out.println(" Senha: admin");
            System.out.println("---------------------------------");
        }
    }
}