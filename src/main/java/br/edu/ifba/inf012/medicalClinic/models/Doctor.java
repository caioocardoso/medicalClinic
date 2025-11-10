package br.edu.ifba.inf012.medicalClinic.models;

import jakarta.persistence.*;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true, updatable = false)
    private String mail;
    private String phone;
    @Column(nullable = false, unique = true, updatable = false)
    private String crm;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Speciality speciality;
    @Embedded
    private Address address;

    
}
