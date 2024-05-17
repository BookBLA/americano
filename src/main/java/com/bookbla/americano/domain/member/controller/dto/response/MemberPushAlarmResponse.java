package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberPushAlarmResponse {

    private final Long memberId;
    private final List<PushAlarmInfo> pushAlarmInfos;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PushAlarmInfo {
        private final Long memberPushAlarmId;
        private final String title;
        private final String body;
    }

    public static MemberPushAlarmResponse from(Member member,
        List<MemberPushAlarm> memberPushAlarms) {
        List<PushAlarmInfo> pushAlarmInfos = memberPushAlarms.stream()
            .map(alarm -> PushAlarmInfo.builder()
                .memberPushAlarmId(alarm.getId())
                .title(alarm.getTitle())
                .body(alarm.getBody())
                .build())
            .collect(Collectors.toList());

        return MemberPushAlarmResponse.builder()
            .memberId(member.getId())
            .pushAlarmInfos(pushAlarmInfos)
            .build();
    }
}
