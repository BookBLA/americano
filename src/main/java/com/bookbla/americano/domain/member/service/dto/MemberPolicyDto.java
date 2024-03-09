package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.repository.entity.Policy;
import java.util.List;
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

    private List<Boolean> agreedStatuses;

    public MemberPolicy toEntity(Member member, Policy policy, Boolean agreedStatus) {
        return MemberPolicy.builder()
            .member(member)
            .policy(policy)
            .agreedStatus(agreedStatus)
            .build();
    }

}
