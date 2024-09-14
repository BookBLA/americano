package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
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
    private final PostcardRepository postcardRepository;

    final int MAX_RECOMMEND = 4; // 하루 최대 4명

    public List<MemberIntroResponse> getRecommendationList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<Postcard> postcards = postcardRepository.findBySendMember(member);

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberBooks);
        List<Member> recommendationMembers = memberRepository.getRecommendationMembers(memberRecommendationDto, postcards);

        List<MemberIntroResponse> memberIntroResponses = new ArrayList<>();
        for (Member recommendationMember : recommendationMembers) {
            MemberBook recommendationMemberBook = memberBookRepository.findByMemberOrderByCreatedAt(recommendationMember).get(0);
            memberIntroResponses.add(MemberIntroResponse.from(recommendationMember, recommendationMemberBook));
        }

        return getDailyMemberIntroResponses(memberIntroResponses);
    }
    private List<MemberIntroResponse> getDailyMemberIntroResponses(List<MemberIntroResponse> memberIntroResponses) {
        if (memberIntroResponses.size() > MAX_RECOMMEND) {
            return memberIntroResponses.subList(0, MAX_RECOMMEND);
        }
        return memberIntroResponses;
    }
}