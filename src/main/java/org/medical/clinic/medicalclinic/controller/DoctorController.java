package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller("/medico")
public class DoctorController {
    DoctorService service;

    @PostMapping
    public void newDoctor(@RequestBody DoctorDTO newDoctor){
        service.newDoctor();

    }
}
