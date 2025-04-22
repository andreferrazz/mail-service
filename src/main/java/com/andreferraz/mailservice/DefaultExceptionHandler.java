package com.andreferraz.mailservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
        var errorMessage = "Could not send e-mail through smtp server";
        log.error(errorMessage, e);
        var response = new ErrorResponse(
                INTERNAL_SERVER_ERROR.getReasonPhrase(),
                errorMessage,
                INTERNAL_SERVER_ERROR.value(),
                req.getServletPath()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}
