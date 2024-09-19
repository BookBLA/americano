package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 주석은 추후 삭제 예정
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingService {

    final int MAX_RECOMMEND = 4; // 하루 최대 4명

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberMatchingRepository memberMatchingRepository;
    private final MemberMatchingFilter memberMatchingFilter;

    public List<MemberIntroResponse> getRecommendationList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        // 사용자의 책과 같다면 우선순위 부여
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);

        // 사용자 기준으로 매칭 생성
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> MemberMatching.builder().member(member).build());

        member.updateLastUsedAt();

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberMatching.getExcluded());

        // 추천회원 id와 추천회원의 책 id 추출
        List<Map<Long, Long>> matchingMembers = memberMatchingRepository
                .getMatchingMemberList(memberRecommendationDto);

        // 학생증 인증 필터링
        matchingMembers = memberMatchingFilter.memberVerifyFiltering(matchingMembers);

        // "거절 + 14일 < 오늘" 필터링
        matchingMembers = memberMatchingFilter.memberRefusedAtFiltering(matchingMembers);

        // response 생성 후 반환
        List<MemberIntroResponse> memberIntroResponses = new ArrayList<>();
        for (Map<Long, Long> matchingMember : matchingMembers) {
            Long matchedMemberId = matchingMember.keySet().iterator().next();
            Long matchedMemberBookId = matchingMember.get(matchedMemberId);

            Member matchedMember = memberRepository.getByIdOrThrow(matchedMemberId);
            MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedMemberBookId);

            if (matchedMember != null && matchedMemberBook != null) {
                memberIntroResponses.add(MemberIntroResponse.from(matchedMember, matchedMemberBook));
            }
        }

        Collections.shuffle(memberIntroResponses);

        return getDailyMemberIntroResponses(memberIntroResponses);
    }

    private List<MemberIntroResponse> getDailyMemberIntroResponses(List<MemberIntroResponse> memberIntroResponses) {
        if (memberIntroResponses.size() > MAX_RECOMMEND) {
            return memberIntroResponses.subList(0, MAX_RECOMMEND);
        } else if (memberIntroResponses.isEmpty()){
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }
        return memberIntroResponses;
    }
}