package com.andreferraz.mail_sender;

public record ErrorResponse(String title,
                            String detail,
                            int status,
                            String path) {
}
