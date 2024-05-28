package com.bookbla.americano.domain.alarm.service.impl;

import com.bookbla.americano.domain.alarm.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.alarm.service.MemberTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberTokenServiceImpl implements MemberTokenService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberTokenCreateResponse createPushToken(
            Long memberId,
            MemberTokenCreateRequest memberTokenCreateRequest
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updatePushToken(memberTokenCreateRequest.getToken());
        return MemberTokenCreateResponse.from(member);
    }

}
