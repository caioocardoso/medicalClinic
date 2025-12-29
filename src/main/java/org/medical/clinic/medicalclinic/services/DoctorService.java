package org.medical.clinic.medicalclinic.services;

import jakarta.validation.Valid;
import org.medical.clinic.medicalclinic.DTO.DoctorDTO;
import org.medical.clinic.medicalclinic.models.Doctor;
import org.medical.clinic.medicalclinic.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository repository;

    public DoctorService(DoctorRepository repository) {
        this.repository = repository;
    }

    public DoctorDTO newDoctor(@Valid DoctorDTO dto){
        Doctor entity = new Doctor();
        entity.setName(dto.getName());
        entity.setMail(dto.getMail());
        entity.setPhone(dto.getPhone());
        entity.setCrm(dto.getCrm());
        entity.setSpeciality(dto.getSpeciality());
        entity.setAddress(dto.getAddress());
        Doctor saved = repository.save(entity);
        dto.setId(saved.getId());
        return dto;
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
