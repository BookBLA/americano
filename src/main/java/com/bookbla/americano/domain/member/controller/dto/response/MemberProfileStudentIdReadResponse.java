package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileStudentIdReadResponse {

    private String studentIdImageStatus;

    public static MemberProfileStudentIdReadResponse from(MemberProfile memberProfile) {
        return MemberProfileStudentIdReadResponse.builder()
            .studentIdImageStatus(memberProfile.getStudentIdImageStatus().name())
            .build();
    }
}
