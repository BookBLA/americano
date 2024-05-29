package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmResponse;

public interface MemberPushAlarmService {

    MemberPushAlarmResponse readPushAlarm(Long memberId);

    void deletePushAlarm(Long memberId, Long memberPushAlarmId);

    void deleteAllPushAlarm(Long memberId);
}
