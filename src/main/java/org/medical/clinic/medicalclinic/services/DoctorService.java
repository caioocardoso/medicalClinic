package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.models.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public DoctorDTO newDoctor(@Valid DoctorRegistrationData data){
        Doctor doctor = new Doctor(data);
        Doctor saved = repository.save(doctor);
        return new DoctorDTO(saved);
    }

    public List<DoctorDTO> getAllDoctors(){
        List<Doctor> doctors = repository.findAll();
        return doctors.stream().map(doc -> {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doc.getId());
            dto.setName(doc.getName());
            dto.setMail(doc.getMail());
            dto.setPhone(doc.getPhone());
            dto.setCrm(doc.getCrm());
            dto.setSpeciality(doc.getSpeciality());
            dto.setAddress(doc.getAddress());
            return dto;
        }).toList();
    }
}
