package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import java.time.LocalDateTime;
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

    private Boolean adAgreementPolicy;

    public MemberPolicy toEntity() {
        return MemberPolicy.builder()
            .adAgreementPolicy(adAgreementPolicy)
            .adAgreementAt(LocalDateTime.now())
            .build();
    }

}
