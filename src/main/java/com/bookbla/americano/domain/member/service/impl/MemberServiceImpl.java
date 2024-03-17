package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Override
    public MemberStatusResponse readMemberStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberStatusResponse.from(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        update(member, memberUpdateRequest);

        return MemberResponse.from(member);
    }

    private void update(Member member, MemberUpdateRequest request) {
        member.updateOauthEmail(request.getOauthEmail())
            .updateMemberType(request.getMemberType())
            .updateMemberStatus(request.getMemberStatus())
        ;
    }
}
