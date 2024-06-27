package com.bookbla.americano.domain.admin.service;

import java.util.List;

import com.bookbla.americano.domain.admin.service.dto.NotificationDto;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.service.AlarmService;
import com.bookbla.americano.domain.notification.service.NotificationClient;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;


@RequiredArgsConstructor
@Service
public class AdminMarketingService {

    private final NotificationClient notificationClient;
    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final TransactionTemplate transactionTemplate;
    private final AlarmService alarmService;

    public List<NotificationResponse> sendNotification(NotificationDto notificationDto, Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<NotificationResponse> response = notificationClient.send(member.getPushToken(), notificationDto.getTitle(), notificationDto.getContents());

        response.stream()
                .filter(NotificationResponse::isSuccess)
                .forEach(it -> saveMemberPushAlarm(notificationDto, member));
        return response;
    }

    private void saveMemberPushAlarm(NotificationDto notificationDto, Member member) {
        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(notificationDto.getTitle())
                .body(notificationDto.getContents())
                .build();
        transactionTemplate.executeWithoutResult(action -> memberPushAlarmRepository.save(memberPushAlarm));
    }

    public void sendNotifications(NotificationDto dto) {
        alarmService.sendPushAlarmAll(new PushAlarmAllCreateRequest(dto.getTitle(), dto.getContents()));
    }

}
