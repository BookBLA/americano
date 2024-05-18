package com.bookbla.americano.domain.alarm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.log.enums.PushAlarmStatus;
import com.bookbla.americano.base.log.enums.PushAlarmType;
import com.bookbla.americano.base.log.repository.PushAlarmLogRepository;
import com.bookbla.americano.base.log.repository.entity.PushAlarmLog;
import com.bookbla.americano.domain.alarm.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.alarm.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.alarm.service.AlarmService;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmServiceImpl implements AlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final PushAlarmLogRepository pushAlarmLogRepository;

    @Override
    @Transactional
    public PushAlarmCreateResponse sendPushAlarm(PushAlarmCreateRequest pushAlarmCreateRequest) {

        Member member = memberRepository.getByIdOrThrow(pushAlarmCreateRequest.getMemberId());

        if (member.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        sendPushAlarmToExpo(member.getPushToken(), pushAlarmCreateRequest.getTitle(),
                pushAlarmCreateRequest.getBody());

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(pushAlarmCreateRequest.getTitle())
                .body(pushAlarmCreateRequest.getBody())
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);

        return PushAlarmCreateResponse.from(member, pushAlarmCreateRequest.getTitle(),
                pushAlarmCreateRequest.getBody());
    }

    @Override
    @Transactional
    public void sendPushAlarm(Member member, String title, String body) {

        if (member.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        sendPushAlarmToExpo(member.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(title)
                .body(body)
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Transactional
    public void sendPushAlarmToExpo(String token, String title, String body) {

        String exponentPushToken = "ExponentPushToken[" + token + "]";

        if (!PushClient.isExponentPushToken(exponentPushToken)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_EXPO_TOKEN);
        }

        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(exponentPushToken);
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(body);

        List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
        expoPushMessages.add(expoPushMessage);

        PushClient client = null;
        try {
            client = new PushClient();
        } catch (PushClientException e) {
            throw new BaseException(PushAlarmExceptionType.INVALID_PUSH_CLIENT);
        }
        List<List<ExpoPushMessage>> chunks = client.chunkPushNotifications(expoPushMessages);

        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();

        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(client.sendPushNotificationsAsync(chunk));
        }

        // Wait for each completable future to finish
        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                allTickets.addAll(messageReplyFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new BaseException(PushAlarmExceptionType.FAIL_TO_SEND_EXPO_SERVER);
            }
        }

        for (int i = 0; i < allTickets.size(); i++) {
            if (allTickets.get(i).getStatus().toString().equals("error")) {
                PushAlarmLog pushAlarmLog = PushAlarmLog.builder()
                        .token(token)
                        .pushAlarmType(PushAlarmType.EXPO)
                        .title(title)
                        .body(body)
                        .pushAlarmStatus(PushAlarmStatus.FAIL)
                        .build();

                pushAlarmLogRepository.save(pushAlarmLog);
            }
        }
    }

}


