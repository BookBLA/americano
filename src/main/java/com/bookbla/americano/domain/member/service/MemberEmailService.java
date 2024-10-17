package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.domain.member.controller.dto.request.MemberEmailSendRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberEmailVerifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.EmailResponse;
import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import com.bookbla.americano.domain.school.exception.SchoolExceptionType;
import com.bookbla.americano.domain.school.repository.SchoolRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberEmailService {

    private final MemberRepository memberRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;
    private final SchoolRepository schoolRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Transactional
    public EmailResponse sendEmail(Long memberId, MemberEmailSendRequest memberEmailSendRequest) {
        String schoolEmail = memberEmailSendRequest.getSchoolEmail();
        String schoolName = memberEmailSendRequest.getSchoolName();

        memberRepository.findByMemberProfileSchoolEmail(schoolEmail)
                .ifPresent(it ->
                    new BaseException(MemberEmailExceptionType.SEND_EMAIL_FAIL)
                );


        School requestSchool = schoolRepository.findByName(schoolName);

        checkSchoolDomainUrl(requestSchool, schoolEmail);

//        checkDuplicatedEmail(schoolEmail);

        String verifyCode = createVerifyCode();
        sendEmailMessage(schoolEmail, verifyCode);

        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseGet(() -> MemberEmail.builder()
                        .member(member)
                        .schoolEmail(schoolEmail)
                        .verifyCode(verifyCode)
                        .emailVerifyStatus(EmailVerifyStatus.PENDING)
                        .build());

        memberEmail.updateSchoolEmail(schoolEmail)
                .updateVerifyCode(verifyCode)
                .updateEmailVerifyPending();

        memberEmailRepository.save(memberEmail);

        return EmailResponse.from(memberEmail);
    }


    @Transactional
    public EmailResponse verifyEmail(Long memberId, MemberEmailVerifyRequest memberEmailVerifyRequest) {
        String requestSchoolEmail = memberEmailVerifyRequest.getSchoolEmail();
        String requestVerifyCode = memberEmailVerifyRequest.getVerifyCode();

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberEmailExceptionType.EMAIL_NOT_REGISTERED));

        if (!requestVerifyCode.equals(memberEmail.getVerifyCode())) {
            throw new BaseException(MemberEmailExceptionType.NOT_EQUAL_VERIFY_CODE);
        }

        if (!requestSchoolEmail.equals(memberEmail.getSchoolEmail())) {
            throw new BaseException(MemberEmailExceptionType.NOT_EQUAL_SCHOOL_EMAIL);
        }

        memberEmail.updateEmailVerifyDone();
        School school = schoolRepository.findByEmailDomainAndName(memberEmail.getEmailDomain(), memberEmailVerifyRequest.getSchoolName())
                .orElseThrow(() -> new BaseException(SchoolExceptionType.EMAIL_DOMAIN_NOT_FOUND));
        member.updateInvitationCode(createVerifyCode())
                .updateSchool(school);

        // 메일 인증시 멤버 책갈피 엔티티 생성, 중복 생성 방지용 로직 추가
        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseGet(() -> memberBookmarkRepository.save(MemberBookmark.builder()
                        .member(member)
                        .build()));

        int currentMemberCounts = (int) memberRepository.countValidMembers(school.getId());
        school.checkOpen(currentMemberCounts);

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

    private void checkSchoolDomainUrl(School school, String schoolEmail) {
        String emailDomain = schoolEmail.replaceAll("^.*?(?=@[^@]+$)", "");
        if (school == null || !emailDomain.equals(school.getEmailDomain()))
            throw new BaseException(MemberEmailExceptionType.EMAIL_DOMAIN_NOT_EQUAL);
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
