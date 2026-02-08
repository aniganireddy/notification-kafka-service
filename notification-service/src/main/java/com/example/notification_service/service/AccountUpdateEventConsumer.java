package com.example.notification_service.service;

import com.example.notification_service.event.AccountUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountUpdateEventConsumer {

    private final JavaMailSender mailSender;

    @KafkaListener(
            topics = "account-updated-topic",
            groupId = "notification-account-updated-group",
            properties = {
                    "spring.json.value.default.type=com.example.notification_service.event.AccountUpdateEvent"
            }
    )


    public void consume(AccountUpdateEvent event) {

        log.info("Received account update event: {}", event);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmailId());
        message.setSubject("Account Updated - Anigani's Bank");
        message.setText(
                "Dear " + event.getFullName() + ",\n\n" +
                        "Your account details has been updated successfully.\n" +
                        "Full Name: " + event.getFullName() + "\n\n" +
                        "Mobile Number: " + event.getMobileNumber() + "\n\n" +
                        "Thank you for banking with us.\n\n" +
                        "Best regards, \n\n" +
                        "Ani's Bank Team."
        );

        mailSender.send(message);

        log.info("Welcome email sent to {}", event.getEmailId());
    }
}

