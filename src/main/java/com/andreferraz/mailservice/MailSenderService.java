package com.andreferraz.mailservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {

    private final MailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public void send(String name, String email, String message) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailUsername);
        mailMessage.setTo(mailUsername);
        mailMessage.setSubject(String.format("%s says hello!", name));
        mailMessage.setText(String.format("Name: %s\nE-mail: %s\nMessage: %s", name, email, message));
        this.mailSender.send(mailMessage);
    }
}
