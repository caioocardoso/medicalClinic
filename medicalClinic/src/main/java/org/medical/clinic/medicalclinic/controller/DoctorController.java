package org.medical.clinic.medicalclinic.controller;

import org.medical.clinic.medicalclinic.DTO.*;
import org.medical.clinic.medicalclinic.clients.EmailClient;
import org.medical.clinic.medicalclinic.clients.EmailDto;
import org.medical.clinic.medicalclinic.models.DoctorRequest;
import org.medical.clinic.medicalclinic.models.User;
import org.medical.clinic.medicalclinic.services.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    DoctorService service;
    @Autowired
    private EmailClient emailClient;

    @GetMapping
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(
            @PageableDefault(size = 10, sort = "user.name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DoctorDTO> doctors = service.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/me")
    public ResponseEntity<DoctorProfileDTO> getMyData(@AuthenticationPrincipal User user) {
        DoctorProfileDTO doctor = service.getDoctorProfileByUserId(user.getId());
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = service.getDoctorDtoById(id);
        return ResponseEntity.ok(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorUpdateData updateDoctor) {
        DoctorDTO updated = service.updateDoctor(id, updateDoctor);

        try {
            EmailDto email = new EmailDto(updated.getEmail(), "Seu cadastro médico foi atualizado com sucesso!", "Olá " + updated.getName() + ", seus dados médicos foram atualizados com sucesso no nosso sistema.");
            emailClient.sendEmail(email);
            logger.info("Email de atualização enviado para: {}", updated.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de atualização para {}: {}", updated.getEmail(), e.getMessage());
        }

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorDTO> deleteDoctor(@PathVariable Long id){
        DoctorDTO deleted = service.deleteDoctor(id);

        try {
            EmailDto email = new EmailDto(deleted.getEmail(), "Seu cadastro de médico foi removido!", "Olá " + deleted.getName() + ", seu cadastro como médico foi excluído com sucesso do nosso sistema. Se foi um engano, entre em contato com o suporte para mais informações.");
            emailClient.sendEmail(email);
            logger.info("Email de exclusão enviado para: {}", deleted.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de exclusão para {}: {}", deleted.getEmail(), e.getMessage());
        }

        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/solicitar-cadastro")
    public ResponseEntity<DoctorRequestDTO> registerDoctorRequest(@AuthenticationPrincipal User user, @Valid @RequestBody DoctorRegistrationData doctorRegistrationData){
        DoctorRequest doctorRequest = new DoctorRequest(user, doctorRegistrationData);
        DoctorRequestDTO doctorRequestDTO = service.saveDoctorRequest(doctorRequest);

        try {
            EmailDto email = new EmailDto(user.getEmail(), "Você solicitou cadastro no nosso sistema!", "Olá " + user.getName() + ", você silicitou cadastro médico no nosso sistema, aguarde algum administrador aceitar a sua solicitação, nós avisaremos você!!");
            emailClient.sendEmail(email);
            logger.info("Email de solicitação enviado para: {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de solicitação para {}: {}", user.getEmail(), e.getMessage());
        }

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

        try {
            EmailDto email;
            if(approvalData.isApproved())
                email = new EmailDto(doctorRequestDTO.userDTO().email(), "Sua solicitação médica foi aprovada!", "Olá " + doctorRequestDTO.userDTO().name() + ", parabéns! Sua solicitação de cadastro médico no nosso sistema foi aprovada. Agora você pode realizar o login e começar a utilizar nossos serviços.");
            else
                email = new EmailDto(doctorRequestDTO.userDTO().email(), "Houve uma alteração na sua solicitação médica!", "Olá " + doctorRequestDTO.userDTO().name() + ", infelizmente sua solicitação de cadastro médico no nosso sistema foi recusada. Caso queira, você pode tentar novamente realizando uma nova solicitação no nosso sistema.");
            emailClient.sendEmail(email);
            logger.info("Email de aprovação/recusa enviado para: {}", doctorRequestDTO.userDTO().email());
        } catch (Exception e) {
            logger.error("Falha ao enviar email de aprovação/recusa para {}: {}", doctorRequestDTO.userDTO().email(), e.getMessage());
        }

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