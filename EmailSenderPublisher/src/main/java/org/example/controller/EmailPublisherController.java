package org.example.controller;

import org.example.dto.EmailDto;
import org.example.service.EmailPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class EmailPublisherController {

    @Autowired
    private EmailPublisherService emailPublisherService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody EmailDto emailDto) {
        emailPublisherService.sendEmail(emailDto);
        return ResponseEntity.ok("Email sent to queue successfully");
    }
}
