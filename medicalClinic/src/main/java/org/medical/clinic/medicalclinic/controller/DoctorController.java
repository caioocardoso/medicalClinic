package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.models.DoctorRequest;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/medico")
@Validated
public class DoctorController {
    @Autowired
    DoctorService service;

    @GetMapping
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(
            @PageableDefault(size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DoctorDTO> doctors = service.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = service.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateData updateDoctor) {
        DoctorDTO updated = service.updateDoctor(id, updateDoctor);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable Long id){
        DoctorDTO deleted = service.deleteDoctor(id);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/solicitar-cadastro")
    public ResponseEntity<DoctorRequestDTO> registerDoctorRequest(@AuthenticationPrincipal User user, @Valid @RequestBody DoctorRegistrationData doctorRegistrationData){
        DoctorRequest doctorRequest = new DoctorRequest(user, doctorRegistrationData);
        DoctorRequestDTO doctorRequestDTO = service.saveDoctorRequest(doctorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorRequestDTO);
    }

    @PostMapping("/cadastrar-novo")
    public ResponseEntity<DoctorRequestDTO> registerNewDoctorRequest(@Valid @RequestBody FullDoctorRegistrationDTO data){
        DoctorRequestDTO doctorRequestDTO = service.registerNewDoctorRequest(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorRequestDTO);
    }

    @GetMapping("/listar-solicitacoes")
    public ResponseEntity<Page<DoctorRequestDTO>> listDoctorRequests(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<DoctorRequestDTO> doctorRequests = service.listDoctorRequests(pageable);
        return ResponseEntity.ok(doctorRequests);
    }

    @PostMapping("/aceitar-cadastro")
    public ResponseEntity<DoctorRequestDTO> approveDoctorRegistration(@RequestBody ApprovalDoctorRequest approvalData){
        DoctorRequestDTO doctorRequestDTO = service.approveDoctorRequest(approvalData);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(doctorRequestDTO);
    }

    @PostMapping("/{id}/perfil")
    public ResponseEntity<DoctorDTO> promoteUserToDoctor(
            @PathVariable Long id,
            @RequestBody @Valid DoctorRegistrationData data) {

        DoctorDTO doctorDTO = service.addDoctorProfileToUserById(id, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorDTO);
    }
}