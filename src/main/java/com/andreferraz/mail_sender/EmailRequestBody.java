package com.andreferraz.mail_sender;

public record EmailRequestBody(String senderName,
                               String senderEmailAddress,
                               String text) {
}
