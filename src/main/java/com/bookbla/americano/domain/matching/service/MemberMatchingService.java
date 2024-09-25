package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MatchIgnoredInfo;
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
    private final MatchedInfoRepository matchedInfoRepository;
    private final MatchExcludedRepository matchExcludedRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;

    public MemberIntroResponse getRecommendationMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseGet(() -> memberMatchingRepository.save(MemberMatching.of(member)));

        member.updateLastUsedAt();

        List<MatchedInfo> matchedMemberList = matchedInfoRepository.findAllByMemberMatchingId(memberMatching.getId());

        if (!matchedMemberList.isEmpty()) {
            /** 조건 전부 쿼리에서 조건 걸고 땡겨옴
             *   회원 탈퇴 (O)
             *   매칭 비활성화 (O)
             *   신고 (O)
             *   차단 (O)
             *   엽서 (X) -> 엽서 거절 부분 못함 그외 ok
             **/

            return buildMemberIntroResponse(getMostPriorityMatched(matchedMemberList));
        }

        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member);

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

        matchedInfoRepository.saveAll(recommendedMembers);

        recommendedMembers = matchedInfoRepository.getAllByDesc(memberMatching.getId());

        return buildMemberIntroResponse(getMostPriorityMatched(recommendedMembers));
    }

    public MemberIntroResponse refreshMemberMatching(Long memberId, Long refreshMemberId, Long refreshMemberBookId) {
        matchIgnoredRepository.findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(memberId, refreshMemberId, refreshMemberBookId)
                .orElseGet(() -> matchIgnoredRepository.save(MatchIgnoredInfo.from(memberId, refreshMemberId, refreshMemberBookId)));

        MemberMatching memberMatching = memberMatchingRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST));

        List<MatchedInfo> recommendedMembers = matchedInfoRepository.findAllByMemberMatchingId(memberMatching.getId());

        return buildMemberIntroResponse(popMostPriorityMatched(recommendedMembers));
    }

    public void rejectMemberMatching(Long memberId, Long rejectedMemberId) {
        matchExcludedRepository.findByMemberIdAndExcludedMemberId(memberId, rejectedMemberId)
                .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(memberId, rejectedMemberId)));
    }

    private MemberIntroResponse buildMemberIntroResponse(MatchedInfo matchedInfo) {
        Member matchedMember = memberRepository.getByIdOrThrow(matchedInfo.getMatchedMemberId());
        MemberBook matchedMemberBook = memberBookRepository.getByIdOrThrow(matchedInfo.getMatchedMemberBookId());

        if (matchedMember == null || matchedMemberBook == null) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }
        return MemberIntroResponse.from(matchedMember, matchedMemberBook);
    }

    private MatchedInfo getMostPriorityMatched(List<MatchedInfo> matchedMemberList) {
        if (matchedMemberList.isEmpty()) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }

        return popMostPriorityMatched(matchedMemberList);
    }

    private MatchedInfo popMostPriorityMatched(List<MatchedInfo> matchedMemberList) {
        if (matchedMemberList.isEmpty()) {
            throw new BaseException(MemberMatchingExceptionType.MATCHING_MEMBER_DOESNT_EXIST);
        }
        MatchedInfo matchedInfo = matchedMemberList.get(0);

        matchedMemberList.remove(0);
        matchedInfoRepository.delete(matchedInfo);
        return matchedInfo;
    }
}