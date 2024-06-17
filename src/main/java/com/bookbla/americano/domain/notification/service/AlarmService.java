package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;

public interface AlarmService {

    void sendPushAlarmForReceivePostCard(Member sendMember, Member receiveMember);

    void sendPushAlarmForAcceptPostcard(Member sendMember, Member receiveMember);

    PushAlarmAllCreateResponse sendPushAlarmAll(PushAlarmAllCreateRequest pushAlarmAllCreateRequest);
}
