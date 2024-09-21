package com.bookbla.americano.domain.notification.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class MemberPushAlarmReadResponses {
    private final long totalCount;
    private final List<MemberPushAlarmReadResponse> data;

    public static MemberPushAlarmReadResponses from(long totalCount, List<MemberPushAlarm> memberPushAlarms) {
        List<MemberPushAlarmReadResponse> memberPushAlarmReadResponses = memberPushAlarms.stream()
            .map(MemberPushAlarmReadResponse::from)
            .collect(Collectors.toList());

        return MemberPushAlarmReadResponses.builder()
            .totalCount(totalCount)
            .data(memberPushAlarmReadResponses)
            .build();
    }

    @AllArgsConstructor
    @Builder
    @Getter
    public static class MemberPushAlarmReadResponse {
        private final Long memberPushAlarmId;
        private String title;
        private String body;
        private String createdAt;

        public static MemberPushAlarmReadResponse from(MemberPushAlarm memberPushAlarm) {
            return MemberPushAlarmReadResponse.builder()
                .memberPushAlarmId(memberPushAlarm.getId())
                .title(memberPushAlarm.getTitle())
                .body(memberPushAlarm.getBody())
                .createdAt(memberPushAlarm.getCreatedAt().toString())
                .build();
        }
    }
}
