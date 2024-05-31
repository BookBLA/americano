package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmAllDeleteResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmDeleteResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmReadResponse;

public interface MemberPushAlarmService {

    MemberPushAlarmReadResponse readPushAlarm(Long memberId);

    MemberPushAlarmDeleteResponse deletePushAlarm(Long memberId, Long memberPushAlarmId);

    MemberPushAlarmAllDeleteResponse deleteAllPushAlarm(Long memberId);
}
