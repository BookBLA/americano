package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileCreateResponse {

    private Long id;

    public static MemberProfileCreateResponse from(MemberProfile memberProfile) {
        return new MemberProfileCreateResponse(memberProfile.getId());
    }
}
