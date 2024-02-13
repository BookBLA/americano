package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateResponse {

    private final Long id;

    public static MemberCreateResponse from(MemberProfile memberProfile) {
        return new MemberCreateResponse(memberProfile.getId());
    }

    public static MemberCreateResponse from(MemberAuth memberAuth) {
        return new MemberCreateResponse(memberAuth.getId());
    }

}
