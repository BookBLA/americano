package com.bookbla.americano.domain.test.service;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.test.controller.dto.response.TestCreateResponse;
import com.bookbla.americano.domain.test.controller.dto.response.TestReadResponse;
import com.bookbla.americano.domain.test.repository.TestRepository;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import com.bookbla.americano.domain.test.service.dto.TestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TestCreateResponse create(TestDto testDto) {
        TestEntity testEntity = testRepository.save(testDto.toEntity());
        return TestCreateResponse.from(testEntity);
    }

    @Transactional(readOnly = true)
    public List<TestReadResponse> findTestsByContents(String contents) {
        return testRepository.findByContents(contents).stream()
                .map(TestReadResponse::from)
                .collect(Collectors.toList());
    }

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
