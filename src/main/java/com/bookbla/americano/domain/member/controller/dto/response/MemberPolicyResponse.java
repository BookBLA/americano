package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberPolicyResponse {

    private final Long memberId;
    private AgreedStatuses agreedStatuses;

    @Getter
    @Builder
    public static class AgreedStatuses {

        private Boolean adAgreementPolicy;

    }

    public static MemberPolicyResponse from(Member member, MemberPolicy memberPolicy) {
        return MemberPolicyResponse.builder()
                .memberId(member.getId())
                .agreedStatuses(AgreedStatuses.builder()
                        .adAgreementPolicy(memberPolicy.getAdAgreementPolicy())
                        .build())
                .build();
    }


}
