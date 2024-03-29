package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStyleRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberStyleServiceImpl implements MemberStyleService {

    private final MemberRepository memberRepository;
    private final MemberStyleRepository memberStyleRepository;
    private final MemberAskRepository memberAskRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberExceptionType.STYLE_NOT_REGISTERED));
        MemberAsk memberAsk  = memberAskRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));

        return MemberStyleResponse.of(member, memberStyle, memberAsk);
    }

    @Override
    public MemberStyleResponse createMemberStyle(Long memberId, MemberStyleCreateRequest memberStyleCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleCreateRequest.toMemberStyleWith(member);
        MemberAsk memberAsk = MemberAsk.builder()
                .member(member)
                .contents(memberStyleCreateRequest.getMemberAsk())
                .build();

        MemberStyle savedMemberStyle = memberStyleRepository.save(memberStyle);
        MemberAsk savedMemberAsk = memberAskRepository.save(memberAsk);

        return MemberStyleResponse.of(member, savedMemberStyle, savedMemberAsk);
    }

    @Override
    public void updateMemberStyle(Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberExceptionType.STYLE_NOT_REGISTERED));
        MemberAsk memberAsk  = memberAskRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));

        memberAsk.updateContent(memberStyleUpdateRequest.getMemberAsk());
        updateMemberStyle(memberStyle, memberStyleUpdateRequest);
    }

    private void updateMemberStyle(MemberStyle memberStyle, MemberStyleUpdateRequest request) {
        memberStyle.updateMbti(request.getMbti())
                .updateDrinkType(request.getDrinkType())
                .updateDateCostType(request.getDateCostType())
                .updateSmokeType(request.getSmokeType())
                .updateContactType(request.getContactType())
                .updateDateStyleType(request.getDateStyleType())
                .updateJustFriendType(request.getJustFriendType());
    }

}
