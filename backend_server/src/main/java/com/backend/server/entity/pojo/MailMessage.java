package com.backend.server.entity.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailMessage {

    @Value("${spring.mail.username}")
    private String fromMail;

    private MailMessage() {}

    public SimpleMailMessage create(String toMail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromMail);
        message.setTo(toMail);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

}
