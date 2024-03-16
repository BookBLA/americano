package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.MailVerifyStatus;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberAuthDto {

    private Member member;
    private String schoolEmail;
    private LocalDateTime emailVerifyStartTime;
    private String phoneNumber;
    private String studentIdImageUrl;

    public MemberAuth toEntity(Member member, String emailVerifyCode) {
        return MemberAuth.builder()
            .member(member)
            .schoolEmail(schoolEmail)
            .emailVerifyCode(emailVerifyCode)
            .emailVerifyStartTime(LocalDateTime.now())
            .phoneNumber(phoneNumber)
            .studentIdImageUrl(studentIdImageUrl)
            .mailVerifyStatus(MailVerifyStatus.PENDING)
            .build();
    }

}
