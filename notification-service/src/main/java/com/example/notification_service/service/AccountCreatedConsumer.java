package com.example.notification_service.service;

import com.example.notification_service.event.AccountCreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCreatedConsumer {

    private final JavaMailSender mailSender;

    @KafkaListener(
            topics = "account-created-topic",
            groupId = "notification-group"
    )
    public void consume(AccountCreationEvent event) {

        log.info("Received account creation event: {}", event);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmailId());
        message.setSubject("Welcome to Our Bank");
        message.setText(
                "Dear " + event.getFullName() + ",\n\n" +
                        "Your account has been created successfully.\n" +
                        "Account Number: " + event.getAccountNumber() + "\n\n" +
                        "Thank you for banking with us.\n\n" +
                        "— Bank Team"
        );

        mailSender.send(message);

        log.info("Welcome email sent to {}", event.getEmailId());
    }
}
