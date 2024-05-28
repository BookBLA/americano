package com.bookbla.americano.domain.alarm.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberPushAlarmDeleteResponse {

    private final Long memberId;
    private final Long memberPushAlarmId;

    public static MemberPushAlarmDeleteResponse from(Member member, Long memberPushAlarmId) {
        return MemberPushAlarmDeleteResponse.builder()
            .memberId(member.getId())
            .memberPushAlarmId(memberPushAlarmId)
            .build();
    }

}
