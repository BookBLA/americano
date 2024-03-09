package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
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
        private Boolean testAgreementPolicy;

    }

    public static MemberPolicyResponse from(Member member, List<MemberPolicy> memberPolicies) {
        return MemberPolicyResponse.builder()
            .memberId(member.getId())
            .agreedStatuses(AgreedStatuses.builder()
                .adAgreementPolicy(memberPolicies.get(0).getAgreedStatus())
                .testAgreementPolicy(memberPolicies.get(1).getAgreedStatus())
                .build())
            .build();
    }



}
