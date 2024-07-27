package com.bookbla.americano.domain.school.event;

import java.util.Optional;

import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
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

    private final MemberBookmarkRepository memberBookmarkRepository;
    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void invitationBookmarkEvent(MemberBookmark invitedmemberBookmark) {
        Long joinMemberId = invitedmemberBookmark.getMember().getId();
        Optional<Invitation> maybeInvitation = invitationRepository.findByInvitedMemberId(joinMemberId);
        if (maybeInvitation.isEmpty()) {
            return;
        }

        Invitation invitation = maybeInvitation.get();
        Member invitedMember = memberRepository.getByIdOrThrow(invitation.getInvitedMemberId());

        if (invitedMember.isWoman()) {
            Optional<MemberBookmark> maybeInvitingMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(invitation.getInvitingMemberId());

            if (maybeInvitingMemberBookmark.isPresent()) {

                invitedmemberBookmark.addInvitationBookmark();
                maybeInvitingMemberBookmark.get().addInvitationBookmark();
            }
        }
    }
}
