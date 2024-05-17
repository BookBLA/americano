package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberPushAlarmAllDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPushAlarmDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPushAlarmResponse;
import org.springframework.transaction.annotation.Transactional;

public interface MemberPushAlarmService {

    MemberPushAlarmResponse readPushAlarm(Long memberId);

    MemberPushAlarmDeleteResponse deletePushAlarm(Long memberId, Long memberPushAlarmId);


    MemberPushAlarmAllDeleteResponse deleteAllPushAlarm(Long memberId);
}
