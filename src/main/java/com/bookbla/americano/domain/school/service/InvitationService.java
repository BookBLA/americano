package com.bookbla.americano.domain.school.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import com.bookbla.americano.domain.school.repository.InvitationRepository;
import com.bookbla.americano.domain.school.repository.entity.Invitation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class InvitationService {

    private final MemberRepository memberRepository;
    private final InvitationRepository invitationRepository;

    @Transactional(readOnly = true)
    public MemberInvitationResponse getInvitationCode(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberInvitationResponse.from(member);
    }

    public InvitationResponse entryInvitationCode(Long memberId, InvitationCodeEntryRequest request) {
        Member invitingMember = memberRepository.findByInvitationCode(request.getInvitationCode())
                .orElseThrow(() -> new BaseException(MemberExceptionType.INVITATION_CODE_NOT_FOUND));

        invitationRepository.findByInvitedMemberId(memberId)
                .orElseGet(() -> invite(memberId, invitingMember));

        return InvitationResponse.from(invitingMember);
    }

    private Invitation invite(Long invitedMemberId, Member invitingMember) {
        Invitation invitation = Invitation.builder()
                .invitedMemberId(invitedMemberId)
                .invitingMemberId(invitingMember.getId())
                .build();
        return invitationRepository.save(invitation);
    }
}
