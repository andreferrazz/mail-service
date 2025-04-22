package com.andreferraz.mailservice;

public record ErrorResponse(String title,
                            String detail,
                            int status,
                            String path) {
}
