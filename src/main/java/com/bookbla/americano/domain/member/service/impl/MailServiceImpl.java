package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.MailService;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final MemberAuthRepository memberAuthRepository;
    private final MemberAuthService memberAuthService;

    @Override
    @Transactional
    public void sendEmail(Member member, String schoolEmail) {
        String title = "테스트용 이메일입니다.";
        String verifyCode = this.createVerifyCode();

        checkDuplicatedEmail(schoolEmail);

        SimpleMailMessage emailForm = createEmailForm(schoolEmail, title, verifyCode);

        try {
            javaMailSender.send(emailForm);
        } catch (Exception e) {
            log.debug("MailService.sendEmail exception occur schoolEmail: {}, "
                + "title: {}, mailAuthCode: {}", schoolEmail, title, verifyCode);
            log.error("email send error", e);
        }

        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
            .member(member)
            .schoolEmail(schoolEmail)
            .emailVerifyCode(verifyCode)
            .build();

        memberAuthService.createMemberAuth(memberAuthDto);
    }

    @Override
    @Transactional
    public void verifyEmail(Member member, String inputVerifyCode) {
        MemberAuth memberAuth = memberAuthRepository.findByMember(member)
            .orElseThrow(() -> new IllegalArgumentException("error"));

        LocalDateTime nowTime = LocalDateTime.now();

        String verifyCode = memberAuth.getEmailVerifyCode();
        LocalDateTime verifyTime = memberAuth.getEmailVerifyStartTime();

        log.info("MemberAuth : " + verifyCode, verifyTime);
        log.info("input data : " + inputVerifyCode, nowTime);

        // nowTime과 verifyTime 사이의 차이 계산
        Duration duration = Duration.between(nowTime, verifyTime);

        // 인증 코드가 다르다면
        if (!verifyCode.equals(inputVerifyCode)) {
            throw new BaseException(MailExceptionType.NOT_EQUAL_VERIFY_CODE);
        }

        // 차이가 30분을 넘어 간다면
        if (duration.toMinutes() > 30) {
            throw new BaseException(MailExceptionType.EXPIRED_TIME);
        }

    }

    private void checkDuplicatedEmail(String email) {
        Optional<MemberAuth> memberAuth = memberAuthRepository.findBySchoolEmail(email);
        if (memberAuth.isPresent()) {
            throw new BaseException(MailExceptionType.ALREADY_EXIST);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);

        return message;
    }

    private String createVerifyCode() {
        int length = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("MailService.createCode() exception occur" + e);
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }

}
