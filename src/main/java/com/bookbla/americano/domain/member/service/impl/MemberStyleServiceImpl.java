package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStyleRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberStyleServiceImpl implements MemberStyleService {

    private final MemberRepository memberRepository;
    private final MemberStyleRepository memberStyleRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);

        return MemberStyleResponse.of(member, memberStyle);
    }

    @Override
    public MemberStyleResponse createMemberStyle(Long memberId, MemberStyleCreateRequest memberStyleCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleCreateRequest.toMemberStyleWith(member);

        MemberStyle savedMemberStyle = memberStyleRepository.save(memberStyle);

        return MemberStyleResponse.of(member, savedMemberStyle);
    }

    @Override
    public void updateMemberStyle(Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);

        update(memberStyle, memberStyleUpdateRequest);
    }

    private void update(MemberStyle memberStyle, MemberStyleUpdateRequest request) {
        memberStyle.updateMbti(request.getMbti())
                .updateDrinkType(request.getDrinkType())
                .updateDateCostType(request.getDateCostType())
                .updateSmokeType(request.getSmokeType())
                .updateContactType(request.getContactType())
                .updateDateStyleType(request.getDateStyleType())
                .updateJustFriendType(request.getJustFriendType());
    }

}
