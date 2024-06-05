package com.bookbla.americano.domain.notification.controller.dto.response;

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
public class MemberPushAlarmReadResponse {

    private final Long memberId;
    private final List<PushAlarmInfo> pushAlarmInfos;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PushAlarmInfo {
        private final Long memberPushAlarmId;
        private final String title;
        private final String body;
        private final String createdAt;
    }

    public static MemberPushAlarmReadResponse from(Member member,
        List<MemberPushAlarm> memberPushAlarms) {
        List<PushAlarmInfo> pushAlarmInfos = memberPushAlarms.stream()
            .map(alarm -> PushAlarmInfo.builder()
                .memberPushAlarmId(alarm.getId())
                .title(alarm.getTitle())
                .body(alarm.getBody())
                .createdAt(alarm.getCreatedAt().toString())
                .build())
            .collect(Collectors.toList());

        return MemberPushAlarmReadResponse.builder()
            .memberId(member.getId())
            .pushAlarmInfos(pushAlarmInfos)
            .build();
    }
}
