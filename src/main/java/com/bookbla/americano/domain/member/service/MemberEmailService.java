package com.bookbla.americano.domain.member.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.base.utils.RedisUtil;
import com.bookbla.americano.domain.member.controller.dto.request.EmailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.EmailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.EmailResponse;
import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
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
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberEmailService {

    private final MemberRepository memberRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MemberPostcardRepository memberPostcardRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final RedisUtil redisUtil;

    @Transactional
    public EmailResponse sendEmail(Long memberId, EmailSendRequest emailSendRequest) {
        String schoolEmail = emailSendRequest.getSchoolEmail();

        // 이메일 중복 체크
        checkDuplicatedEmail(schoolEmail);

        // 이메일 보내기
        String verifyCode = createVerifyCode();
        sendEmailMessage(schoolEmail, verifyCode);

        // 5분 뒤에 redis 만료
        redisUtil.setDataExpire(verifyCode, schoolEmail, 5 * 60);
        redisUtil.setDataExpire(schoolEmail, verifyCode, 5 * 60);

        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseGet(() -> MemberEmail.builder()
                        .member(member)
                        .schoolEmail(schoolEmail)
                        .emailVerifyStatus(EmailVerifyStatus.PENDING)
                        .build());

        memberEmail.updateSchoolEmail(schoolEmail)
                .updateEmailVerifyPending();

        memberEmailRepository.save(memberEmail);

        return EmailResponse.from(memberEmail);
    }

    @Transactional
    public EmailResponse verifyEmail(Long memberId, EmailVerifyRequest emailVerifyRequest) {
        String requestSchoolEmail = emailVerifyRequest.getSchoolEmail();
        String requestVerifyCode = emailVerifyRequest.getVerifyCode();

        String redisSchoolEmail = redisUtil.getData(requestVerifyCode);
        String redisVerifyCode = redisUtil.getData(requestSchoolEmail);

        if (!requestSchoolEmail.equals(redisSchoolEmail) ||
                !requestVerifyCode.equals(redisVerifyCode)) {
            throw new BaseException(MemberEmailExceptionType.NOT_EQUAL_VERIFY_CODE);
        }

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberEmailExceptionType.EMAIL_NOT_REGISTERED));

        if (!requestSchoolEmail.equals(memberEmail.getSchoolEmail())) {
            throw new BaseException(MemberEmailExceptionType.NOT_EQUAL_SCHOOL_EMAIL);
        }

        memberEmail.updateEmailVerifyDone();

        // 메일 인증시 멤버 엽서 엔티티 생성, 중복 생성 방지용 로직 추가
        memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseGet(() -> memberPostcardRepository.save(MemberPostcard.builder()
                        .member(member)
                        .build()));


        redisUtil.deleteData(requestSchoolEmail);
        redisUtil.deleteData(redisVerifyCode);
        return EmailResponse.from(memberEmail);
    }

    private void sendEmailMessage(String schoolEmail, String verifyCode) {
        String subject = "북블라 학교 이메일 인증코드";

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
            throw new BaseException(MemberEmailExceptionType.SEND_EMAIL_FAIL);
        }
    }

    private void checkDuplicatedEmail(String schoolEmail) {
        memberRepository.findByMemberProfileSchoolEmail(schoolEmail)
                .ifPresent(action -> new BaseException(MemberEmailExceptionType.ALREADY_EXIST));
    }

    private String createVerifyCode() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int CODE_LENGTH = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < CODE_LENGTH; i++) {
                int randomIdx = random.nextInt(CHARACTERS.length());
                builder.append(CHARACTERS.charAt(randomIdx));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }
}
