package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberPushAlarmAllDeleteResponse {

    private final Long memberId;

    public static MemberPushAlarmAllDeleteResponse from(Member member) {
        return MemberPushAlarmAllDeleteResponse.builder()
            .memberId(member.getId())
            .build();
    }
}
