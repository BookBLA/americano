package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberAuthStatusResponse {

    private String studentIdImageStatus;

    public static MemberAuthStatusResponse from(MemberAuth memberauth) {
        return MemberAuthStatusResponse.builder()
            .studentIdImageStatus(memberauth.getStudentIdImageStatus().name())
            .build();
    }

}
