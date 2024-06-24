package com.bookbla.americano.domain.notification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.notification.enums.PushAlarmStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmType;
import com.bookbla.americano.domain.notification.repository.PushAlarmLogRepository;
import com.bookbla.americano.domain.notification.repository.entity.PushAlarmLog;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.notification.service.AlarmService;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private static final String POSTCARD_SEND_TITLE = "띵동~\uD83D\uDC8C 엽서가 도착했어요!";
    private static final String POSTCARD_SEND_BODY = "%s님이 엽서를 보냈어요! 지금 확인해 보세요~\uD83E\uDD70";
    private static final String POSTCARD_ACCEPT_TITLE = "축하합니다\uD83E\uDD73\uD83E\uDD73 매칭에 성공하셨습니다~!!\uD83D\uDC95";
    private static final String POSTCARD_ACCEPT_BODY = "%s님이 엽서를 수락했어요! 지금 바로 채팅해보세요~\uD83E\uDD70";

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final PushAlarmLogRepository pushAlarmLogRepository;

    @Override
    @Transactional
    public void sendPushAlarmForReceivePostCard(Member sendMember, Member receiveMember) {
        // 해당 멤버가 푸시 토큰이 없다면 에러 발생
        if (receiveMember.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        // 해당 멤버가 회원가입 완료상태가 아니라면
        if (!receiveMember.getMemberStatus().equals(MemberStatus.COMPLETED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        // 엽서 도착은 익명 처리
        String body = String.format(POSTCARD_SEND_BODY, sendMember.getMemberProfile().showBlindName());
        sendToExpo(receiveMember.getPushToken(), POSTCARD_SEND_TITLE, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(receiveMember)
                .title(POSTCARD_SEND_TITLE)
                .body(body)
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Override
    @Transactional
    public void sendPushAlarmForAcceptPostcard(Member sendMember, Member receiveMember) {
        // 해당 멤버가 푸시 토큰이 없다면 에러 발생
        if (sendMember.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!sendMember.getMemberStatus().equals(MemberStatus.COMPLETED)
                && !sendMember.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        // 엽서 수락은 실명 처리
        String body = String.format(POSTCARD_ACCEPT_BODY, receiveMember.getMemberProfile().getName());

        sendToExpo(sendMember.getPushToken(), POSTCARD_ACCEPT_TITLE, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(sendMember)
                .title(POSTCARD_ACCEPT_TITLE)
                .body(body)
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Override
    @Transactional
    public PushAlarmAllCreateResponse sendPushAlarmAll(
            PushAlarmAllCreateRequest pushAlarmAllCreateRequest
    ) {

        String title = pushAlarmAllCreateRequest.getTitle();
        String body = pushAlarmAllCreateRequest.getBody();

        List<Member> selectedMembers = memberRepository.findByMemberStatus(MemberStatus.COMPLETED,
                MemberStatus.MATCHING_DISABLED);

        List<String> tokens = selectedMembers.stream()
                .map(Member::getPushToken)
                .collect(Collectors.toList());

        // https://docs.expo.dev/push-notifications/sending-notifications/#request-errors
        List<List<String>> tokenBatches = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i += 100) {
            int end = Math.min(tokens.size(), i + 100);
            List<String> batch = tokens.subList(i, end);
            tokenBatches.add(new ArrayList<>(batch)); // Add the batch to the list of batches
        }

        for (List<String> tokenBatch : tokenBatches) {
            sendListToExpo(tokenBatch, title, body);
        }

        for (Member member : selectedMembers) {
            MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                    .member(member)
                    .title(title)
                    .body(body)
                    .build();

            memberPushAlarmRepository.save(memberPushAlarm);
        }

        return PushAlarmAllCreateResponse.from(title, body);
    }

    private void sendListToExpo(List<String> tokens, String title, String body) {
        List<String> exponentPushTokens = tokens.stream()
                .map(token -> "ExponentPushToken[" + token + "]")
                .collect(Collectors.toList());

        ExpoPushMessage expoPushMessage = new ExpoPushMessage();

        for (String exponentPushToken : exponentPushTokens) {
            expoPushMessage.getTo().add(exponentPushToken);
        }

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
                        .token(tokens.get(i))
                        .pushAlarmType(PushAlarmType.EXPO)
                        .title(title)
                        .body(body)
                        .pushAlarmStatus(PushAlarmStatus.FAIL)
                        .build();

                pushAlarmLogRepository.save(pushAlarmLog);
            }
        }

    }

    private void sendToExpo(String token, String title, String body) {

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


