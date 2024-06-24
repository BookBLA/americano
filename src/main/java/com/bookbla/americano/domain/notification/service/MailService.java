package com.bookbla.americano.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.username}")
    String adminEmail;


    public void sendTransactionFailureEmail(String transactionName, String message) {
        String title = String.format("[BookBLA-Backend]Transaction Fail - %s", transactionName);
        SimpleMailMessage emailForm = createEmailForm(adminEmail, title, message);

        try {
            javaMailSender.send(emailForm);
        } catch (Exception e) {
            log.debug("MailService.sendEmail exception occur email: {}, "
                    + "title: {}, message: {}", adminEmail, title, message);
            log.error("email send error", e);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }
}
