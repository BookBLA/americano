package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import com.bookbla.americano.domain.school.repository.entity.InvitationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
