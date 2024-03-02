package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.Policy;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberPolicyCreateRequest {

    @NotNull(message = "선택약관의 동의여부가 입력되지 않았습니다.")
    private Boolean agreedStatus;

    public MemberPolicyDto toDto(Member member, Policy policy) {
        return MemberPolicyDto.builder()
            .member(member)
            .policy(policy)
            .agreedStatus(agreedStatus)
            .build();
    }

}
