package com.bookbla.americano.domain.notification.event;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import com.bookbla.americano.domain.notification.service.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;

import static com.bookbla.americano.domain.notification.enums.PushAlarmForm.POSTCARD_ACCEPT;

@RequiredArgsConstructor
@Component
public class PushAlarmEventHandler {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final NotificationClient notificationClient;
    private final TransactionTemplate txTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendPostcard(PostcardAlarmEvent postcardAlarmEvent) {
        Member targetMember = postcardAlarmEvent.getPostcardAcceptMember();
        if (targetMember.canNotSendPushAlarm()) {
            return;
        }

        // 해당 멤버가 회원가입 완료상태가 아니라면
        if (!targetMember.getMemberStatus().equals(MemberStatus.COMPLETED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        notificationClient.sendWithForm(targetMember.getPushToken(), PushAlarmForm.POSTCARD_SEND);

        txTemplate.executeWithoutResult(it -> memberPushAlarmRepository.save(
                MemberPushAlarm.fromPushAlarmForm(targetMember, PushAlarmForm.POSTCARD_SEND))
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void acceptPostcard(PostcardAlarmEvent postcardAlarmEvent) {
        Member targetMember = postcardAlarmEvent.getTargetMember();
        if (targetMember.canNotSendPushAlarm()) {
            return;
        }

        // 해당 멤버가 회원가입 완료상태가 아니면서 매칭 비활성화가 아니라면
        if (!targetMember.getMemberStatus().equals(MemberStatus.COMPLETED)
                && !targetMember.getMemberStatus().equals(MemberStatus.MATCHING_DISABLED)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_MEMBER_STATUS);
        }

        String title = POSTCARD_ACCEPT.getTitle();
        String body = PushAlarmForm.getBodyWithFormat(
                postcardAlarmEvent.getPostcardAcceptMember().getMemberProfile().getName(),
                POSTCARD_ACCEPT
        );

        notificationClient.send(targetMember.getPushToken(), title, body);

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(targetMember)
                .title(title)
                .body(body)
                .build();
        txTemplate.executeWithoutResult(it -> memberPushAlarmRepository.save(memberPushAlarm));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendInvitationSuccessMessage(Member member) {
        notificationClient.sendWithForm(
                member.getPushToken(),
                PushAlarmForm.INVITATION_SUCCESS
        );

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.fromPushAlarmForm(member, PushAlarmForm.INVITATION_SUCCESS);
        txTemplate.executeWithoutResult(it -> memberPushAlarmRepository.save(memberPushAlarm));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendAll(PushAlarmAllCreateRequest pushAlarmAllCreateRequest) {
        List<Member> canSendPushAlarmMembers = memberRepository.findAll()
                .stream()
                .filter(Member::canSendPushAlarm)
                .collect(Collectors.toList());

        List<String> tokens = canSendPushAlarmMembers.stream()
                .map(Member::getPushToken)
                .collect(Collectors.toList());

        notificationClient.sendAll(tokens,
                pushAlarmAllCreateRequest.getTitle(),
                pushAlarmAllCreateRequest.getBody()
        );

        canSendPushAlarmMembers.forEach(member ->
                txTemplate.executeWithoutResult(
                        none -> memberPushAlarmRepository.save(MemberPushAlarm.builder()
                                .member(member)
                                .title(pushAlarmAllCreateRequest.getTitle())
                                .body(pushAlarmAllCreateRequest.getBody())
                                .build())
                )
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendByAdmin(Member member, PushAlarmForm pushAlarmForm) {
        notificationClient.send(
                member.getPushToken(),
                pushAlarmForm.getTitle(),
                pushAlarmForm.getBody()
        );

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(pushAlarmForm.getTitle())
                .body(pushAlarmForm.getBody())
                .build();
        txTemplate.executeWithoutResult(it -> memberPushAlarmRepository.save(memberPushAlarm));
    }
}
