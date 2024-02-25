package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthUpdateResponse;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository;

    @Override
    public void createMemberAuth(MemberAuthDto memberAuthDto) {
        memberAuthRepository.save(memberAuthDto.toEntity());
    }

    @Override
    @Transactional
    public MemberAuthUpdateResponse updateMemberAuth(Long memberId,
        String phoneNumber, String studentIdImageUrl) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberAuth memberAuth = memberAuthRepository.findByMember(member)
            .orElseThrow(() -> new IllegalArgumentException("Not found member_id"));

        memberAuth.updateMemberAuth(phoneNumber, studentIdImageUrl);

        return MemberAuthUpdateResponse.from(memberAuth);
    }

}
