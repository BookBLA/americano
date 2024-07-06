package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberEmailResponse {
    private String schoolEmail;
    private String emailVerifyStatus;

    public static MemberEmailResponse from(MemberEmail memberEmail) {
        return MemberEmailResponse.builder()
            .schoolEmail(memberEmail.getSchoolEmail())
            .emailVerifyStatus(memberEmail.getEmailVerifyStatus().name())
            .build();
    }
}

