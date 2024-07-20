package com.bookbla.americano.domain.notification.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PushAlarmSettingResponse {

    private final Boolean pushAlarmEnabled;

    public static PushAlarmSettingResponse from(Member member) {
        return PushAlarmSettingResponse.builder()
            .pushAlarmEnabled(member.getPushAlarmEnabled())
            .build();
    }

}
