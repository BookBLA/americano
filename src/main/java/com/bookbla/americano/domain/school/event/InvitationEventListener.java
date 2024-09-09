package com.bookbla.americano.domain.school.event;

import com.bookbla.americano.domain.admin.event.AdminNotificationEventListener;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEvent;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class InvitationEventListener {

    private final PushAlarmEventHandler pushAlarmEventHandler;
    private final AdminNotificationEventListener adminNotificationEventListener;
    private final MemberBookmarkRepository memberBookmarkRepository;
    private final InvitationRepository invitationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void invitationBookmarkEvent(MemberBookmark invitedMemberBookmark) {
        Long joinMemberId = invitedMemberBookmark.getMember().getId();
        invitationRepository.findByInvitedMemberId(joinMemberId)
                .ifPresent(invitation -> processInvitation(invitation, invitedMemberBookmark));
    }

    private void processInvitation(Invitation invitation, MemberBookmark invitedMemberBookmark) {
        if (invitation.isFestivalTemporaryInvitation()) {
            invitedMemberBookmark.addBookmark(105);
            adminNotificationEventListener.sendMessage(new AdminNotificationEvent("축제 코드 입력 +1", "memberId " + invitation.getInvitedMemberId()));
            invitation.bookmark();
            return;
        }

        memberBookmarkRepository.findMemberBookmarkByMemberId(invitation.getInvitingMemberId())
                .ifPresent(it -> handleInvitationsByGender(invitation, invitedMemberBookmark, it));
    }

    private void handleInvitationsByGender(
            Invitation invitation,
            MemberBookmark invitedMemberBookmark,
            MemberBookmark invitingMemberBookmark
    ) {
        if (invitation.isWomanInvitation()) {
            invitedMemberBookmark.addWomanInvitationBookmark();
            invitingMemberBookmark.addWomanInvitationBookmark();
        }

        if (invitation.isManInvitation()) {
            invitedMemberBookmark.addManInvitationBookmark();
            invitingMemberBookmark.addManInvitationBookmark();
        }

        pushAlarmEventHandler.sendInvitationSuccessMessage(invitingMemberBookmark.getMember());
        invitation.bookmark();
    }
}
