package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberContactReadResponse {

    private Long memberId;
    private List<String> phoneNumbers;

    public static MemberContactReadResponse from(Member member, List<String> phoneNumbers) {
        return MemberContactReadResponse.builder()
            .memberId(member.getId())
            .phoneNumbers(phoneNumbers)
            .build();
    }
}
