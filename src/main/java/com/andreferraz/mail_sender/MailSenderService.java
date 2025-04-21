package com.andreferraz.mail_sender;

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
    private String to;

    public void send(String senderName, String senderEmailAddress, String text) {
        var message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(to);
        message.setSubject(String.format("%s says hello!", senderName));
        message.setText(text);
        this.mailSender.send(message);
    }
}
