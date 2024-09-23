package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final MemberMatchingAlgorithmFilter memberMatchingAlgorithmFilter;

    public List<MemberIntroResponse> getRecommendationList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> MemberMatching.of(member));

        member.updateLastUsedAt();

        List<MatchedInfo> matchedMemberList = memberMatching.getMatched();
        List<MemberIntroResponse> memberIntroResponses = new ArrayList<>();

        if (!matchedMemberList.isEmpty()) {
            // TODO: 회원 탈퇴, 신고, 엽서, 매칭 비활성화, 차단 검증하는 부분 추가

            buildMemberIntroResponses(matchedMemberList, memberIntroResponses);

            return getDailyMemberIntroResponses(memberIntroResponses);
        }

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberMatching.getExcluded());

        // 추천회원 id와 추천회원의 책 id 추출
        List<MatchedInfo> matchingMembers = memberMatchingRepository
                .getMatchingMembers(memberRecommendationDto);

        // 차단한 회원 필터링
        matchingMembers = memberMatchingFilter.memberBlockedFiltering(member.getId(), matchingMembers);

        // 학생증 인증 필터링
        matchingMembers = memberMatchingFilter.memberVerifyFiltering(matchingMembers);

        // "거절 + 14일 < 오늘" 필터링
        matchingMembers = memberMatchingFilter.memberRefusedAtFiltering(member.getId(), matchingMembers);

        // 우선순위 알고리즘 적용
        memberMatchingAlgorithmFilter.memberMatchingAlgorithmFiltering(member, matchingMembers);

        // 추천회원 matched에 저장
        memberMatching.updateMatched(matchingMembers);

        // 추천회원 matched 정렬
        memberMatching.sortMatched();

        buildMemberIntroResponses(matchingMembers, memberIntroResponses);

        return getDailyMemberIntroResponses(memberIntroResponses);
    }

    public void rejectMemberMatching(Long memberId, Long rejectedMemberId, Long rejectedMemberBookId) {
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MATCHING));

        memberMatching.addIgnoredMemberAndBook(rejectedMemberId, rejectedMemberBookId);
    }

    private void buildMemberIntroResponses(List<MatchedInfo> matchedMemberList, List<MemberIntroResponse> memberIntroResponses) {
        for (MatchedInfo matchedInfo : matchedMemberList) {
            Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
            MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

            if (matchedMember != null && matchedMemberBook != null) {
                memberIntroResponses.add(MemberIntroResponse.from(matchedMember, matchedMemberBook));
            }
        }
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