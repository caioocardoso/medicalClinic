package org.example.consumer;

import org.example.dto.EmailDto;
import org.example.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    private final EmailService emailService;

    @Autowired
    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${mq.queues.email}")
    public void listen(@Payload EmailDto emailDto) {
        System.out.println("Message received: " + emailDto);
        emailService.sendEmail(emailDto);
    }
}
