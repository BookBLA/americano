package com.bookbla.americano.domain.notification.service.impl;

import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.log.enums.PushAlarmStatus;
import com.bookbla.americano.base.log.enums.PushAlarmType;
import com.bookbla.americano.base.log.repository.PushAlarmLogRepository;
import com.bookbla.americano.base.log.repository.entity.PushAlarmLog;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.notification.service.AlarmService;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import java.util.stream.Collectors;
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

        // 해당 멤버가 푸시 토큰이 없다면 에러 발생
        if (member.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!member.getMemberStatus().equals(MemberStatus.COMPLETED)
            && !member.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        sendToExpo(member.getPushToken(), pushAlarmCreateRequest.getTitle(),
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
    public void sendPushAlarmForReceivePostCard(Member receiveMember) {
        // 해당 멤버가 푸시 토큰이 없다면 에러 발생
        if (receiveMember.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!receiveMember.getMemberStatus().equals(MemberStatus.COMPLETED)
            && !receiveMember.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        String title = "띵동~\uD83D\uDC8C 엽서가 도착했어요!";
        String body = receiveMember.getMemberProfile().getName() + "님이 엽서를 보냈어요! 지금 확인해 보세요~\uD83E\uDD70";

        sendToExpo(receiveMember.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
            .member(receiveMember)
            .title(title)
            .body(body)
            .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Override
    @Transactional
    public void sendPushAlarmForAcceptPostcard(Member sendMember) {
        // 해당 멤버가 푸시 토큰이 없다면 에러 발생
        if (sendMember.getPushToken() == null) {
            throw new BaseException(PushAlarmExceptionType.NOT_FOUND_TOKEN);
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!sendMember.getMemberStatus().equals(MemberStatus.COMPLETED)
            && !sendMember.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        String title = "축하합니다\uD83E\uDD73\uD83E\uDD73 매칭에 성공하셨습니다~!!\uD83D\uDC95";
        String body = sendMember.getMemberProfile().getName() + "님이 엽서를 수락했어요! 지금 바로 채팅해보세요~\uD83E\uDD70";

        sendToExpo(sendMember.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
            .member(sendMember)
            .title(title)
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

        log.info(expoPushMessage.getTitle());
        log.info(expoPushMessage.getBody());

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
            log.info(allTickets.get(i).getStatus().toString());
            log.info(allTickets.get(i).getMessage());

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


