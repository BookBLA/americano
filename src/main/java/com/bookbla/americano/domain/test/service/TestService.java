package com.bookbla.americano.domain.test.service;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member signUpAdmin(String email) {
        return memberRepository.findByMemberTypeAndOauthEmail(MemberType.ADMIN, email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .memberType(MemberType.ADMIN)
                                .memberStatus(MemberStatus.PROFILE)
                                .oauthEmail(email)
                                .build()));
    }

    @Transactional
    public Member signUpKakao(String email) {
        return memberRepository.findByMemberTypeAndOauthEmail(MemberType.KAKAO, email)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .memberType(MemberType.KAKAO)
                                .memberStatus(MemberStatus.PROFILE)
                                .oauthEmail(email)
                                .build()));
    }
}
