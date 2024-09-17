package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationRewardResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberModal;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import com.bookbla.americano.domain.school.repository.entity.InvitationStatus;
import com.bookbla.americano.domain.school.repository.entity.InvitationType;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public MemberInvitationResponse getInvitationCode(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberInvitationResponse.from(member);
    }

    public InvitationResponse entryInvitationCode(Long invitedMemberId, InvitationCodeEntryRequest request) {
        if (isFestivalTemporaryInvitationCode(request.getInvitationCode())) {
            Invitation invitation = invitationRepository.save(Invitation.fromTempFestival(invitedMemberId));
            return InvitationResponse.from(invitation);
        }

        Member invitingMember = memberRepository.findByInvitationCode(request.getInvitationCode())
                .orElseThrow(() -> new BaseException(MemberExceptionType.INVITATION_CODE_NOT_FOUND));
        Member invitedMember = memberRepository.getByIdOrThrow(invitedMemberId);

        invitedMember.getMemberModal().updateMemberInvited(InvitationStatus.BOOKMARK);
        invitingMember.getMemberModal().getInviting().put(invitedMemberId, Boolean.FALSE);

        Invitation invitation = invitationRepository.findByInvitedMemberId(invitedMemberId)
                .orElseGet(() -> createInvitationWithInvitedMemberGender(invitedMember, invitingMember));

        return InvitationResponse.from(invitation);
    }

    private boolean isFestivalTemporaryInvitationCode(String invitationCode) {
        return FESTIVAL_TEMPORARY_INVITATION_CODE.equals(invitationCode);
    }

    private Invitation createInvitationWithInvitedMemberGender(Member invitedMember, Member invitingMember) {
        InvitationType invitationType = invitedMember.isWoman() ? WOMAN : MAN;

        Invitation invitation = Invitation.builder()
                .invitedMemberId(invitedMember.getId())
                .invitingMemberId(invitingMember.getId())
                .invitationType(invitationType)
                .build();

        return invitationRepository.save(invitation);
    }

    public MemberInvitationRewardResponse getInvitationRewardStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberModal modal = member.getMemberModal();

        boolean invitingReward = false;
        boolean invitedReward = false;

        Map<Long, Boolean> inviting = modal.getInviting();
        for (Map.Entry<Long, Boolean> entry : inviting.entrySet()) {
            if (!entry.getValue()) {
                entry.setValue(Boolean.TRUE);
                invitingReward = true;
                break;
            }
        }

        if (modal.getInvited() == InvitationStatus.BOOKMARK) {
            modal.updateMemberInvited(InvitationStatus.COMPLETED);
            invitedReward = true;
        }

        return MemberInvitationRewardResponse.from(invitingReward, invitedReward);
    }

    public MemberInvitationRewardResponse updateInvitationRewardStatus(Long memberId, String invitationStatus) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        member.getMemberModal().updateMemberInvited(InvitationStatus.from(invitationStatus));

        return getInvitationRewardStatus(memberId);
    }
}
