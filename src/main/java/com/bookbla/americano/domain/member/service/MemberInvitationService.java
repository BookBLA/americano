package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberInvitationEntryRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPostcardResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberInvitationService {

    private final MemberRepository memberRepository;
    private final MemberPostcardRepository memberPostcardRepository;

    @Transactional(readOnly = true)
    public MemberInvitationResponse getInvitationCode(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberInvitationResponse.from(member);
    }

    public MemberPostcardResponse entryInvitationCode(Long memberId, MemberInvitationEntryRequest request) {
        Member invitatingMember = memberRepository.findByInvitationCode(request.getInvitationCode())
                .orElseThrow(() -> new BaseException(MemberExceptionType.INVITATION_CODE_NOT_FOUND));
        if (invitatingMember.isMan()) {
            return MemberPostcardResponse.none();
        }

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPostcard memberPostcard = memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseGet(() -> createMemberPostcard(member));

        memberPostcard.addInvitationPostcard();
        return MemberPostcardResponse.from(memberPostcard);
    }

    private MemberPostcard createMemberPostcard(Member member) {
        return memberPostcardRepository.save(MemberPostcard.builder()
                .member(member)
                .build());
    }
}
