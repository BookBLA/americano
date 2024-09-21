package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.event.AdminNotificationEventListener;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;

import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationRewardResponse;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberModal;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEvent;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import com.bookbla.americano.domain.school.exception.InvitationExceptionType;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import com.bookbla.americano.domain.school.repository.entity.InvitationStatus;
import com.bookbla.americano.domain.school.repository.entity.InvitationType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.bookbla.americano.domain.school.repository.entity.InvitationType.*;


/*
 * 8. 15 도현님 요청
 * 축제용 임시 초대코드 발급
 * */
@RequiredArgsConstructor
@Transactional
@Service
public class InvitationService {

    private static final String FESTIVAL_TEMPORARY_INVITATION_CODE = "JUST4YOU";

    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    private final PushAlarmEventHandler pushAlarmEventHandler;
    private final AdminNotificationEventListener adminNotificationEventListener;

    @Transactional(readOnly = true)
    public MemberInvitationResponse getInvitationCode(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberInvitationResponse.from(member);
    }

    public InvitationResponse entryInvitationCode(Long invitedMemberId, InvitationCodeEntryRequest request) {
        validateInvitation(invitedMemberId, request.getInvitationCode());

        if (FESTIVAL_TEMPORARY_INVITATION_CODE.equals(request.getInvitationCode())) {
            Invitation invitation = createFestivalInvitation(invitedMemberId);
            invitation.bookmark();
            return InvitationResponse.from(invitation);
        }

        Member invitingMember = memberRepository.findByInvitationCode(request.getInvitationCode())
                .orElseThrow(() -> new BaseException(MemberExceptionType.INVITATION_CODE_NOT_FOUND));
        Member invitedMember = memberRepository.getByIdOrThrow(invitedMemberId);

        invitedMember.getMemberModal().updateMemberInvitedRewardStatus(InvitationStatus.BOOKMARK);
        invitingMember.getMemberModal().getInvitingRewardStatus().put(invitedMemberId, Boolean.FALSE);

        Invitation invitation = invitationRepository.findByInvitedMemberId(invitedMemberId)
                .orElseGet(() -> createInvitation(invitedMember.isWoman(), invitingMember.getId(), invitedMemberId));

        processInvitationReward(invitation, invitedMemberId, invitingMember.getId());

        return InvitationResponse.from(invitation);
    }

    private void validateInvitation(Long invitedMemberId, String invitationCode) {
        if (invitationRepository.existsByInvitedMemberId(invitedMemberId)) {
            throw new BaseException(InvitationExceptionType.INVITATION_EXISTS);
        }

        Member member = memberRepository.getByIdOrThrow(invitedMemberId);
        if (member.getInvitationCode().equals(invitationCode)) {
            throw new BaseException(InvitationExceptionType.INVALID_INVITATION_CODE_MYSELF);
        }
    }

    private @NotNull Invitation createFestivalInvitation(Long invitedMemberId) {
        MemberBookmark invitedMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(invitedMemberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));
        invitedMemberBookmark.addBookmark(105);

        adminNotificationEventListener.sendMessage(new AdminNotificationEvent("축제 코드 입력 +1", "memberId " + invitedMemberId));

        return invitationRepository.save(Invitation.fromTempFestival(invitedMemberId));
    }

    private Invitation createInvitation(
            boolean isWoman,
            Long invitingMemberId,
            Long invitedMemberId
    ) {
        InvitationType invitationType = isWoman ? WOMAN : MAN;

        Invitation invitation = Invitation.builder()
                .invitingMemberId(invitingMemberId)
                .invitedMemberId(invitedMemberId)
                .invitationType(invitationType)
                .build();

        return invitationRepository.save(invitation);
    }
    
        private void processInvitationReward(
            Invitation invitation,
            Long invitedMemberId,
            Long invitingMemberId
    ) {
        if (invitation.isComplete()) {
            return;
        }

        MemberBookmark invitedMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(invitedMemberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));
        MemberBookmark invitingMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(invitingMemberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));

        rewardBookmark(invitation, invitedMemberBookmark, invitingMemberBookmark);

        pushAlarmEventHandler.sendInvitationSuccessMessage(invitingMemberBookmark.getMember());
        invitation.bookmark();
    }

    private void rewardBookmark(
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
    }

    public MemberInvitationRewardResponse getInvitationRewardStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberModal modal = member.getMemberModal();

        boolean invitingReward = false;
        Long invitedMembersId = null;

        for (Map.Entry<Long, Boolean> entry : modal.getInvitingRewardStatus().entrySet()) {
            if (!entry.getValue()) {
                entry.setValue(Boolean.TRUE);
                invitedMembersId = entry.getKey();
                invitingReward = true;
            }
        }

        boolean invitedReward = modal.getAndUpdateInvitedRewardStatus();

        if (invitingReward) {
            Member invitedMember = memberRepository.getByIdOrThrow(invitedMembersId);
            return MemberInvitationRewardResponse.from(true, invitedReward, invitedMember.getMemberProfile().getGender());
        }

        return MemberInvitationRewardResponse.from(false, invitedReward);
    }

    public MemberInvitationRewardResponse updateInvitationRewardStatus(Long memberId, InvitationStatus invitationStatus) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        member.getMemberModal().updateMemberInvitedRewardStatus(invitationStatus);

        return getInvitationRewardStatus(memberId);
    }
}
