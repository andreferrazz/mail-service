package com.andreferraz.mailservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MailSenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MailSender mailSender;

    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    private static final String REFERER = "https://example.com/contact";

    @Test
    void whenPostEmail_thenRedirectToReferer() throws Exception {
        var request = post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "André")
                .param("email", "andre@gmail.com")
                .param("message", "Hi!")
                .header("Referer", REFERER);
        mockMvc.perform(request)
                .andExpect(status().isFound())
                .andExpect(header().string("Location", REFERER));
    }

    @Test
    void givenAnAllowedOrigin_whenPerformRequest_returnRedirectStatus() throws Exception {
        for (var origin : allowedOrigins) {
            var request = post("/email")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("name", "André")
                    .param("email", "andre@gmail.com")
                    .param("message", "Hi!")
                    .header("Origin", origin)
                    .header("Referer", REFERER);
            mockMvc.perform(request)
                    .andExpect(status().isFound())
                    .andExpect(header().string("Location", REFERER))
                    .andExpect(header().string("Access-Control-Allow-Origin", origin));
        }
    }

    @Test
    void givenAForbiddenOrigin_whenPerformRequest_returnForbiddenStatus() throws Exception {
        if (Arrays.asList(allowedOrigins).contains("*")) {
            return;
        }
        var request = post("/email").header("Origin", "https://non-allowed-origin.com");
        mockMvc.perform(request)
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void whenPostEmailWithJsonAccept_thenReturn201() throws Exception {
        var request = post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "André")
                .param("email", "andre@gmail.com")
                .param("message", "Hi!");
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("E-mail successfully sent"));
    }

    @Test
    void whenMailExceptionWithJsonAccept_thenReturn500() throws Exception {
        doThrow(new MailSendException("SMTP error"))
                .when(mailSender).send(any(SimpleMailMessage.class));
        var request = post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "André")
                .param("email", "andre@gmail.com")
                .param("message", "Hi!");
        mockMvc.perform(request)
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    void whenMailExceptionThrown_thenRedirectToRefererWithError() throws Exception {
        doThrow(new MailSendException("SMTP error"))
                .when(mailSender).send(any(SimpleMailMessage.class));
        var request = post("/email")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "André")
                .param("email", "andre@gmail.com")
                .param("message", "Hi!")
                .header("Referer", REFERER);
        mockMvc.perform(request)
                .andExpect(status().isFound())
                .andExpect(header().string("Location", REFERER + "?error=true"));
    }
}