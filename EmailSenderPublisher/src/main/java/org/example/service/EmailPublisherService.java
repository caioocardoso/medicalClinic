package org.example.service;

import org.example.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailPublisherService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.email}")
    private String emailQueue;

    public void sendEmail(EmailDto emailDto) {
        rabbitTemplate.convertAndSend(emailQueue, emailDto);
    }
}
