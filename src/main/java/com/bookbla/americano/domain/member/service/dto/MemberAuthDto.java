package com.bookbla.americano.domain.member.service.dto;

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
    private String emailVerifyCode;
    private LocalDateTime emailVerifyStartTime;

    public MemberAuth toEntity() {
        return MemberAuth.builder()
            .member(member)
            .schoolEmail(schoolEmail)
            .emailVerifyCode(emailVerifyCode)
            .emailVerifyStartTime(LocalDateTime.now())
            .build();
    }

}