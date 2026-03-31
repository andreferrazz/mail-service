package com.andreferraz.mailservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailSenderController {

    private final MailSenderService mailSenderService;

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@ModelAttribute EmailRequestBody payload, HttpServletRequest request) {
        mailSenderService.send(payload.name(), payload.email(), payload.message());
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        if (accept != null && accept.contains("application/json")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("message", "E-mail successfully sent"));
        }
        String referer = request.getHeader(HttpHeaders.REFERER);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, referer)
                .build();
    }

}
