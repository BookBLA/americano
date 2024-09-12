package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingService {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;

    public List<MemberIntroResponse> getRecommendationList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        List<Member> recommendationMembers = memberRepository.getRecommendationMembers(member, memberBooks.get(0));

        List<MemberIntroResponse> memberIntroResponses = new ArrayList<>();
        for (Member recommendationMember : recommendationMembers) {
            MemberBook recommendationMemberBook = memberBookRepository.findByMemberOrderByCreatedAt(recommendationMember).get(0);
            memberIntroResponses.add(MemberIntroResponse.from(recommendationMember, recommendationMemberBook));
        }

        if (memberIntroResponses.size() > 4) {
            return memberIntroResponses.subList(0, 4);
        }

        return memberIntroResponses;
    }
}
