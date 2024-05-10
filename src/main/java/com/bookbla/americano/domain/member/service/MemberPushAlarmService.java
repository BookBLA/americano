package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberPushAlarmResponse;

public interface MemberPushAlarmService {

    MemberPushAlarmResponse readPushAlarm(Long memberId);
}
