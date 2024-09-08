package com.bookbla.americano.domain.school.event;

import com.bookbla.americano.domain.admin.event.AdminNotificationEventListener;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
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

@RequiredArgsConstructor
@Component
public class InvitationEventListener {

    private final PushAlarmEventHandler pushAlarmEventHandler;
    private final AdminNotificationEventListener adminNotificationEventListener;
    private final MemberBookmarkRepository memberBookmarkRepository;
    private final MemberRepository memberRepository;
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
            adminNotificationEventListener.sendMessage(new AdminNotificationEvent("[축제 코드 입력 +1]", "memberId " + invitation.getInvitedMemberId()));
            invitation.bookmark();
            return;
        }

        Member invitedMember = memberRepository.getByIdOrThrow(invitation.getInvitedMemberId());

        if (invitedMember.isWoman()) {
            handleWomanInvitation(invitation, invitedMemberBookmark);
            return;
        }
        if (invitedMember.isMan()){
            handleManInvitation(invitation, invitedMemberBookmark);
            return;
        }

        invitation.complete();
    }

    private void handleWomanInvitation(Invitation invitation, MemberBookmark invitedMemberBookmark) {
        memberBookmarkRepository.findMemberBookmarkByMemberId(invitation.getInvitingMemberId())
                .ifPresent(invitingMemberBookmark -> addWomanBookmark(invitedMemberBookmark, invitingMemberBookmark));
        invitation.bookmark();
    }

    private void addWomanBookmark(MemberBookmark invitedmemberBookmark, MemberBookmark invitingMemberBookmark) {
        invitedmemberBookmark.addWomanInvitationBookmark();
        invitingMemberBookmark.addWomanInvitationBookmark();

        pushAlarmEventHandler.sendInvitationSuccessMessage(invitingMemberBookmark.getMember());
    }

    private void handleManInvitation(Invitation invitation, MemberBookmark invitedMemberBookmark) {
        memberBookmarkRepository.findMemberBookmarkByMemberId(invitation.getInvitingMemberId())
                .ifPresent(invitingMemberBookmark -> addManBookmark(invitedMemberBookmark, invitingMemberBookmark));
        invitation.bookmark();
    }

    private void addManBookmark(MemberBookmark invitedmemberBookmark, MemberBookmark invitingMemberBookmark) {
        invitedmemberBookmark.addManInvitationBookmark();
        invitingMemberBookmark.addManInvitationBookmark();

        pushAlarmEventHandler.sendInvitationSuccessMessage(invitingMemberBookmark.getMember());
    }
}
