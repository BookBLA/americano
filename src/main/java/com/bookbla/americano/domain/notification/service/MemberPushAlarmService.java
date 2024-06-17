package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponse;

public interface MemberPushAlarmService {

    MemberPushAlarmReadResponse readPushAlarm(Long memberId);

    void deletePushAlarm(Long memberId, Long memberPushAlarmId);

    void deleteAllPushAlarm(Long memberId);
}
