package com.bookbla.americano.domain.school.event;

import com.bookbla.americano.domain.admin.event.AdminNotificationEventListener;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEvent;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.bookbla.americano.domain.member.enums.Gender.*;

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
            handleFestivalTemporaryInvitation(invitation, invitedMemberBookmark);
            return;
        }

        memberBookmarkRepository.findMemberBookmarkByMemberId(invitation.getInvitingMemberId())
                .ifPresentOrElse(
                        invitingMemberBookmark -> handleInvitationsByGender(invitation, invitedMemberBookmark, invitingMemberBookmark),
                        invitation::complete);
    }

    private void handleFestivalTemporaryInvitation(Invitation invitation, MemberBookmark invitedMemberBookmark) {
        invitedMemberBookmark.addBookmark(105);
        adminNotificationEventListener.sendMessage(new AdminNotificationEvent("[축제 코드 입력 +1]", "memberId " + invitation.getInvitedMemberId()));
        invitation.bookmark();
    }

    private void handleInvitationsByGender(
            Invitation invitation,
            MemberBookmark invitedMemberBookmark,
            MemberBookmark invitingMemberBookmark
    ) {
        if (invitation.isWomanInvitation()) {
            addGenderBookmark(invitedMemberBookmark, invitingMemberBookmark, FEMALE);
        }
        if (invitation.isManInvitation()) {
            addGenderBookmark(invitedMemberBookmark, invitingMemberBookmark, MALE);
        }
        invitation.bookmark();
    }

    private void addGenderBookmark(
            MemberBookmark invitedMemberBookmark,
            MemberBookmark invitingMemberBookmark,
            Gender gender
    ) {
        if (gender == FEMALE) {
            invitedMemberBookmark.addWomanInvitationBookmark();
            invitingMemberBookmark.addWomanInvitationBookmark();
        }
        if (gender == MALE) {
            invitedMemberBookmark.addManInvitationBookmark();
            invitingMemberBookmark.addManInvitationBookmark();
        }
        pushAlarmEventHandler.sendInvitationSuccessMessage(invitingMemberBookmark.getMember());
    }
}
