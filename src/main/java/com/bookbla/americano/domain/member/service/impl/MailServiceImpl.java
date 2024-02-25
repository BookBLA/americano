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
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;
    private final MemberAuthService memberAuthService;

    @Override
    @Transactional
    public void sendEmail(Long memberId, String schoolEmail) {
        checkDuplicatedEmail(schoolEmail);

        String subject = "Bookbla 이메일 인증 테스트입니다.";
        String verifyCode = createVerifyCode();

        Context context = new Context();
        context.setVariable("verifyCode", verifyCode);
        String text = templateEngine.process("verifyEmail", context);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(schoolEmail);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new BaseException(MailExceptionType.SEND_EMAIL_FAIL);
        }

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuthDto memberAuthDto = MemberAuthDto.builder()
            .member(member)
            .schoolEmail(schoolEmail)
            .emailVerifyCode(verifyCode)
            .build();

        memberAuthService.createMemberAuth(memberAuthDto);
    }

    @Override
    @Transactional
    public void verifyEmail(Long memberId, String inputVerifyCode) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.findByMember(member)
            .orElseThrow(() -> new IllegalArgumentException("error"));

        LocalDateTime nowTime = LocalDateTime.now();

        String verifyCode = memberAuth.getEmailVerifyCode();
        LocalDateTime verifyTime = memberAuth.getEmailVerifyStartTime();

        Duration duration = Duration.between(nowTime, verifyTime);

        if (!verifyCode.equals(inputVerifyCode)) {
            throw new BaseException(MailExceptionType.NOT_EQUAL_VERIFY_CODE);
        }

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
