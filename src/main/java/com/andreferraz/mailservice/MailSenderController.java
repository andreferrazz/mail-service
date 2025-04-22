package com.andreferraz.mailservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MailSenderController {

    private final MailSenderService mailSenderService;

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequestBody payload) {
        mailSenderService.send(payload.senderName(), payload.senderEmailAddress(), payload.text());
        return ResponseEntity
                .status(201)
                .body(Map.of("message", "E-mail successfully sent"));
    }

}
