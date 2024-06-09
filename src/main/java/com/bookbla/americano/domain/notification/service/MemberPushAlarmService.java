package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmAllDeleteResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmDeleteResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponse;

public interface MemberPushAlarmService {

    MemberPushAlarmReadResponse readPushAlarm(Long memberId);

    MemberPushAlarmDeleteResponse deletePushAlarm(Long memberId, Long memberPushAlarmId);

    MemberPushAlarmAllDeleteResponse deleteAllPushAlarm(Long memberId);
}
