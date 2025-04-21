package com.andreferraz.mail_sender;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MailSenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    @Test
    void test() throws Exception {
            var request = post("/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"senderName\": \"André\", \"senderEmailAddress\": \"oandreferraz@gmail.com\",  \"text\": \"Hi!\" }");
            mockMvc.perform(request)
                    .andExpect(status().isCreated());
    }

    @Test
    void givenAnAllowedOrigin_whenPerformRequest_returnCreatedStatus() throws Exception {
        for (var origin : allowedOrigins) {
            var request = post("/email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"senderName\": \"André\", \"senderEmailAddress\": \"oandreferraz@gmail.com\",  \"text\": \"Hi!\" }")
                    .header("Origin", origin);
            mockMvc.perform(request)
                    .andExpect(status().isCreated())
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
}