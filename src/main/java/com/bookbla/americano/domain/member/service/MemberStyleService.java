package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.MemberStyle;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStyleRepository;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberStyleService {

    private final MemberRepository memberRepository;
    private final MemberStyleRepository memberStyleRepository;

    @Transactional(readOnly = true)
    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);

        return MemberStyleResponse.of(member, memberStyle);
    }

    public MemberStyleResponse createMemberStyle(Long memberId, MemberStyleCreateRequest memberStyleCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleCreateRequest.toMemberStyleWith(member);

        MemberStyle savedMemberStyle = memberStyleRepository.save(memberStyle);
        return MemberStyleResponse.of(member, savedMemberStyle);
    }

    public void updateMemberStyle(Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);

        update(memberStyle, memberStyleUpdateRequest);
    }

    private void update(MemberStyle memberStyle, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        memberStyle.updateMbti(memberStyleUpdateRequest.getMbti());
        memberStyle.updateDrinkType(memberStyleUpdateRequest.getDrinkType());
        memberStyle.updateDateCostType(memberStyleUpdateRequest.getDateCostType());
        memberStyle.updateSmokeType(memberStyleUpdateRequest.getSmokeType());
        memberStyle.updateContactType(memberStyleUpdateRequest.getContactType());
        memberStyle.updateDateStyle(memberStyleUpdateRequest.getDateStyle());
        memberStyle.updateJustFriendType(memberStyleUpdateRequest.getJustFriendType());
    }

}
