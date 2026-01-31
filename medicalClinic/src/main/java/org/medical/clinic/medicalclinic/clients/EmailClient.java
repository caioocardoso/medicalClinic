package org.medical.clinic.medicalclinic.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("EmailPublisher")
public interface EmailClient {

    @PostMapping("/emails")
    ResponseEntity<String> sendEmail(@RequestBody EmailDto dto);
}
