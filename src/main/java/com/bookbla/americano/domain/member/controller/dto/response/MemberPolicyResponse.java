package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberPolicyResponse {

    private final Long id;

    public static MemberPolicyResponse from(MemberPolicy memberPolicy) {
        return MemberPolicyResponse.builder()
            .id(memberPolicy.getId())
            .build();
    }

}
