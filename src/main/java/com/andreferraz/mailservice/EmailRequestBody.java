package com.andreferraz.mailservice;

public record EmailRequestBody(String name,
                               String email,
                               String message) {
}
