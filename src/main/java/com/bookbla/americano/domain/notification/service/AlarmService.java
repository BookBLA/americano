package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.chat.repository.ChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.MemberChatRoomRepository;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;
import com.bookbla.americano.domain.notification.enums.PushAlarmStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmType;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.notification.repository.PushAlarmLogRepository;
import com.bookbla.americano.domain.notification.repository.entity.PushAlarmLog;
import io.github.jav.exposerversdk.ExpoPushMessage;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.PushClient;
import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final PushAlarmLogRepository pushAlarmLogRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPushAlarmForReceivePostCard(Member sendMember, Member receiveMember) {

        if (isPushAlarmAble(receiveMember)) {
            return;
        }

        // 해당 멤버가 회원가입 완료상태가 아니라면
        if (!receiveMember.getMemberStatus().equals(MemberStatus.COMPLETED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        String title = PushAlarmForm.POSTCARD_SEND.getTitle();
        String body = PushAlarmForm.POSTCARD_SEND.getBody();
        body = String.format(body, sendMember.getMemberProfile().getName());

        sendToExpo(receiveMember.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(receiveMember)
                .title(title)
                .body(body)
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPushAlarmForAcceptPostcard(Member sendMember, Member receiveMember) {

        if (isPushAlarmAble(sendMember)) {
            return;
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!sendMember.getMemberStatus().equals(MemberStatus.COMPLETED)
                && !sendMember.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        String title = PushAlarmForm.POSTCARD_ACCEPT.getTitle();
        String body = PushAlarmForm.POSTCARD_ACCEPT.getBody();
        body = String.format(body, receiveMember.getMemberProfile().getName());

        sendToExpo(sendMember.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(sendMember)
                .title(title)
                .body(body)
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPushAlarmForChat(Member receiver, Chat chat) {
        if (isPushAlarmAble(receiver)) {
            return;
        }

        // Select Member Query 발생 예상(Lazy FetchType)
        String senderName = chat.getSender().getMemberProfile().getName();
        String chatContent = chat.getContent();

        sendToExpo(receiver.getPushToken(), senderName, chatContent);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(receiver)
                .title(senderName)
                .body(chatContent)
                .build();
        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPushAlarmForChat(List<Member> members, Chat chat) {
        // Push 본문
        Member sender = memberRepository.findById(chat.getSender().getId()).orElseThrow();
        String senderName = sender.getMemberProfile().getName();
        String chatContent = chat.getContent();

        List<Member> alarmAbleMember = new ArrayList<>();

        // 채팅방 알람 수신 검사
        for (Member member : members) {
            // TODO: Fetch Join 필요
            MemberChatRoom memberChatRoom = memberChatRoomRepository.findByMember_IdAndChatRoom_Id(member.getId(), chat.getChatRoom().getId())
                    .orElseThrow();

            if (isPushAlarmAble(member) && memberChatRoom.getIsAlert()) {
                alarmAbleMember.add(member);
            }
        }
        sendListToExpo(alarmAbleMember.stream().map(Member::getPushToken)
                .collect(Collectors.toList()), senderName, chatContent);

        for (Member member : alarmAbleMember) {
            MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                    .member(member)
                    .title(senderName)
                    .body(chatContent)
                    .build();
            memberPushAlarmRepository.save(memberPushAlarm);

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPushAlarm(Member member, PushAlarmForm pushAlarmForm) {

        if (isPushAlarmAble(member)) {
            return;
        }

        sendToExpo(member.getPushToken(), pushAlarmForm.getTitle(), pushAlarmForm.getBody());

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(pushAlarmForm.getTitle())
                .body(pushAlarmForm.getBody())
                .build();

        memberPushAlarmRepository.save(memberPushAlarm);
    }

    @Transactional
    public PushAlarmAllCreateResponse sendPushAlarmAll(
            PushAlarmAllCreateRequest pushAlarmAllCreateRequest
    ) {

        String title = pushAlarmAllCreateRequest.getTitle();
        String body = pushAlarmAllCreateRequest.getBody();

        List<Member> selectedMembers = memberRepository.findByMemberStatusAndAdAgreement(MemberStatus.COMPLETED,
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

    private boolean isPushAlarmAble(Member member) {
        return member.getPushToken() != null && member.getPushAlarmEnabled();
    }

}


