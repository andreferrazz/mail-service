package com.andreferraz.mailservice;

public record EmailRequestBody(String senderName,
                               String senderEmailAddress,
                               String text) {
}
