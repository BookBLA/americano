package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingService {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    /**
     * 사용자에게 랜덤 매칭 리스트를 제공
     * 제외 조건
     * 1. 이성만 띄워줌
     * 2. 14일간 접속하지 않은 사용자 제외 -> last_modified_at ??
     * 3. 학생증 인증 이틀 지났을 시 추천에서 제외
     * 4. 이미 매칭되었던 사용자 제외
     */
    public List<Member> getRandomMatchingList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        List<Member> randomMembers = memberRepository.getRandomMembers(member.getMemberProfile().getGender(), member.getMemberMatchIgnores());
        Collections.shuffle(randomMembers);

        if (randomMembers.size() <= 4) {
            return randomMembers;
        } else {
            return randomMembers.subList(0, 4);
        }
    }

    public List<MemberIntroResponse> getRecommendationList(Long memberId, Long memberBookId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberBook memberBook = memberBookRepository.getByIdOrThrow(memberBookId);

        List<Member> recommendationMembers = memberRepository.getRecommendationMembers(member, memberBook);

        return null;
    }

}
