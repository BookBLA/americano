package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberAuthUpdateResponse {

    private Long id;

    public static MemberAuthUpdateResponse from(MemberAuth memberAuth) {
        return new MemberAuthUpdateResponse(memberAuth.getId());
    }
}
