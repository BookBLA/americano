package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.MemberProfile;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileReadResponse;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    public MemberProfileReadResponse getMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);

        return MemberProfileReadResponse.from(memberProfile);
    }

    public Member getMemberById(Long memberId) {
        return memberRepository.getByIdOrThrow(memberId);
    }

}
