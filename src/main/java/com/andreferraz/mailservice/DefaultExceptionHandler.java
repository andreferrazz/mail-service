package com.andreferraz.mailservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(MailException.class)
    public ResponseEntity<?> handleMailException(HttpServletRequest req, Exception e) {
        var errorMessage = "Could not send e-mail through SMTP server";
        log.error(errorMessage, e);

        String accept = req.getHeader(HttpHeaders.ACCEPT);
        if (accept == null || !accept.contains("application/json")) {
            String referer = req.getHeader(HttpHeaders.REFERER);
            if (referer != null) {
                String separator = referer.contains("?") ? "&" : "?";
                String redirectUrl = referer + separator + "error=true";
                return ResponseEntity
                        .status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, redirectUrl)
                        .build();
            }
        }

        var response = new ErrorResponse(
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                errorMessage,
                INTERNAL_SERVER_ERROR.value(),
                req.getServletPath()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}
