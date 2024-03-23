package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.member.controller.dto.request.MailResendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthStatusUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAuthUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MailVerifyResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthStatusResponse;
import com.bookbla.americano.domain.member.exception.MailExceptionType;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;
    private final MemberPostcardRepository memberPostcardRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional
    public MemberAuthResponse sendEmailAndCreateMemberAuth(Long memberId,
        MemberAuthDto memberAuthDto) {
        String schoolEmail = memberAuthDto.getSchoolEmail();

        checkDuplicatedEmail(schoolEmail);
        String emailVerifyCode = sendEmail(schoolEmail);

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.save(
            memberAuthDto.toEntity(member, emailVerifyCode));

        return MemberAuthResponse.from(member, memberAuth);
    }

    @Override
    @Transactional
    public MailVerifyResponse verifyEmail(Long memberId, MailVerifyRequest mailVerifyRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);
        LocalDateTime nowTime = LocalDateTime.now();

        String verifyCode = memberAuth.getEmailVerifyCode();
        LocalDateTime verifyTime = memberAuth.getEmailVerifyStartTime();

        Duration duration = Duration.between(nowTime, verifyTime);

        if (!verifyCode.equals(mailVerifyRequest.getVerifyCode())) {
            throw new BaseException(MailExceptionType.NOT_EQUAL_VERIFY_CODE);
        }

        if (duration.toMinutes() > 5) {
            throw new BaseException(MailExceptionType.EXPIRED_TIME);
        }

        memberAuth.updateMailVerifyDone();

        // 메일 인증시 멤버 엽서 엔티티 생성
        memberPostcardRepository.save(MemberPostcard.builder()
                .member(member)
                .build());

        return MailVerifyResponse.from(memberAuth);
    }

    @Override
    @Transactional
    public MemberAuthResponse resendEmail(Long memberId, MailResendRequest mailResendRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        String schoolEmail = mailResendRequest.getSchoolEmail();

        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);

        String enrolledSchoolEmail = memberAuth.getSchoolEmail();

        if (!schoolEmail.equals(enrolledSchoolEmail)) {
            checkDuplicatedEmail(schoolEmail);
            memberAuth.updateSchoolEmail(schoolEmail);
        }

        String emailVerifyCode = sendEmail(schoolEmail);

        memberAuth.updateEmailVerifyCode(emailVerifyCode)
            .updateEmailVerifyStartTime(LocalDateTime.now());

        return MemberAuthResponse.from(member, memberAuth);
    }

    @Override
    @Transactional
    public MemberAuthResponse readMemberAuth(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);

        return MemberAuthResponse.from(member, memberAuth);
    }


    @Override
    @Transactional
    public MemberAuthResponse updateMemberAuth(Long memberId,
        MemberAuthUpdateRequest memberAuthUpdateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);

        update(memberAuth, memberAuthUpdateRequest);

        return MemberAuthResponse.from(member, memberAuth);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberAuthStatusResponse readMemberAuthStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);

        return MemberAuthStatusResponse.from(memberAuth);
    }

    @Override
    @Transactional
    public MemberAuthStatusResponse updateMemberAuthStatus(Long memberId,
        MemberAuthStatusUpdateRequest memberAuthStatusUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.getByMemberOrThrow(member);

        memberAuth.updateStudentIdImageStatus(
            memberAuthStatusUpdateRequest.getStudentIdImageStatus());

        return MemberAuthStatusResponse.from(memberAuth);
    }


    private void update(MemberAuth memberAuth, MemberAuthUpdateRequest request) {
        memberAuth.updateSchoolEmail(request.getSchoolEmail())
            .updatePhoneNumber(request.getPhoneNumber())
            .updateStudentIdImageUrl(request.getStudentIdImageUrl())
            .updateStudentIdImageStatus(request.getStudentIdImageStatus());
    }

    private String sendEmail(String schoolEmail) {

        String subject = "Bookbla 이메일 인증";
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

        return verifyCode;
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
            throw new BaseException(BaseExceptionType.TEST_FAIL);
        }
    }

}
