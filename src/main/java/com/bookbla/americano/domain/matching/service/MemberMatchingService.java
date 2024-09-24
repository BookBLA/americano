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

import java.util.List;

/**
 * 주석은 추후 삭제 예정
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingService {

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberMatchingRepository memberMatchingRepository;
    private final MemberMatchingFilter memberMatchingFilter;
    private final MemberMatchingAlgorithmFilter memberMatchingAlgorithmFilter;

    public MemberIntroResponse getRecommendationMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> MemberMatching.of(member));

        member.updateLastUsedAt();

        List<MatchedInfo> matchedMemberList = memberMatching.getMatched();

        if (!matchedMemberList.isEmpty()) {
            // TODO: 회원 탈퇴, 신고, 엽서, 매칭 비활성화, 차단 검증하는 부분 추가
            return buildMemberIntroResponse(memberMatching.popMostPriorityMatched());
        }

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberMatching.getExcluded());

        // 추천회원 id와 추천회원의 책 id 추출
        List<MatchedInfo> recommendedMembers = memberMatchingRepository
                .getMatchingMembers(memberRecommendationDto);

        // 차단한 회원 필터링
        recommendedMembers = memberMatchingFilter.memberBlockedFiltering(member.getId(), recommendedMembers);

        // 학생증 인증 필터링
        recommendedMembers = memberMatchingFilter.memberVerifyFiltering(recommendedMembers);

        // "거절 + 14일 < 오늘" 필터링
        recommendedMembers = memberMatchingFilter.memberRefusedAtFiltering(member.getId(), recommendedMembers);

        // 우선순위 알고리즘 적용
        memberMatchingAlgorithmFilter.memberMatchingAlgorithmFiltering(member, recommendedMembers);

        // 추천회원 matched에 저장
        memberMatching.saveAndUpdateMatched(recommendedMembers);

        // 추천회원 matched 정렬
        memberMatching.sortMatched();

        return buildMemberIntroResponse(memberMatching.popMostPriorityMatched());
    }

    public MemberIntroResponse refreshMemberMatching(Long memberId, Long refreshMemberId, Long refreshMemberBookId) {
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MATCHING));

        memberMatching.addIgnoredMemberAndBook(refreshMemberId, refreshMemberBookId);

        return buildMemberIntroResponse(memberMatching.popMostPriorityMatched());
    }

    public void rejectMemberMatching(Long memberId, Long rejectedMemberId) {
        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.NOT_FOUND_MATCHING));

        memberMatching.addExcludedMember(rejectedMemberId);
    }

    private MemberIntroResponse buildMemberIntroResponse(MatchedInfo matchedInfo) {
        Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }
        return MemberIntroResponse.from(matchedMember, matchedMemberBook);
    }
}