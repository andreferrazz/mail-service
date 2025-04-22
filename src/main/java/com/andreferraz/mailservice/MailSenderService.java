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

    public void send(String senderName, String senderEmailAddress, String text) {
        var message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(mailUsername);
        message.setSubject(String.format("%s says hello!", senderName));
        message.setText(String.format("Name: %s\nE-mail: %s\nMessage: %s", senderName, senderEmailAddress, text));
        this.mailSender.send(message);
    }
}
