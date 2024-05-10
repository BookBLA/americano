package com.bookbla.americano.domain.alarm.controller.dto.response;


import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PushAlarmCreateResponse {

    private final Long memberId;
    private final String title;
    private final String body;


    public static PushAlarmCreateResponse from(Member member, String title, String body) {
        return PushAlarmCreateResponse.builder()
            .memberId(member.getId())
            .title(title)
            .body(body)
            .build();
    }

}
