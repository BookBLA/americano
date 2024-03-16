package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final MemberAuthRepository memberAuthRepository;

    @Value("${mail.auth-email}")
    String adminEmail;
    @Override
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
