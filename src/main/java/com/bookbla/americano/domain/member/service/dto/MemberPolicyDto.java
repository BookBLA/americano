package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.repository.entity.Policy;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberPolicyDto {

    private Boolean adAgreementPolicy;

    public MemberPolicy toEntity(Member member, Policy policy) {
        return MemberPolicy.builder()
            .member(member)
            .policy(policy)
            .agreedStatus(adAgreementPolicy)
            .build();
    }

}
