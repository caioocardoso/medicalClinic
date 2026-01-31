package org.example.service.impl;

import org.example.dto.EmailDto;
import org.example.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDto.getTo());
            message.setSubject(emailDto.getSubject());
            message.setText(emailDto.getBody());
            emailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
