package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingFilter {

    private final PostcardRepository postcardRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;
    private final MatchedInfoRepository matchedInfoRepository;
    private final MatchExcludedRepository matchExcludedRepository;

    /**
     * 제외된 회원 필터링
     */
    public List<Long> memberExcludedFiltering(Long appMemberId, List<Long> matchingMemberIds){
        List<Long> filteredMatchMemberIds = matchExcludedRepository.getExcludedMemberIdsByMemberId(appMemberId, matchingMemberIds);

        return matchingMemberIds.stream()
                .filter(memberId -> !filteredMatchMemberIds.contains(memberId))
                .collect(Collectors.toList());
    }

    /**
     * 엽서 거절 조건 필터링
     */
    public List<MatchedInfo> memberRefusedAtFiltering(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto) {

        List<MatchedInfo> matches = matchedInfoRepository.getAllMatches(matchingMemberIds, memberRecommendationDto);

        // 앱 사용자로 부터 받은 엽서를 거절한지 14일이 안된 회원&회원 책 = 추천되면 안되는 회원
        List<MatchedInfo> filteredMatches = postcardRepository.getReceiveIdsAndReceiveMemberBookIdsByRefusedAt(memberRecommendationDto);

        // matches - filteredMatches => 최종적으로 뽑고싶은 추천 memberId & memberBookId
        return matches.stream()
                .filter(match -> filteredMatches.stream()
                        .noneMatch(filteredMatch -> filteredMatch.getMatchedMemberId().equals(match.getMatchedMemberId())
                                                        && filteredMatch.getMatchedMemberBookId().equals(match.getMatchedMemberBookId())))
                .collect(Collectors.toList());
    }

    /**
     * MatchIgnoredInfo 필터링
     */
    public List<MatchedInfo> memberIgnoredFiltering(List<MatchedInfo> recommendedMatches, MemberRecommendationDto memberRecommendationDto) {
        List<MatchedInfo> filteredMatches = matchIgnoredRepository.getIgnoredMemberIdsAndIgnoredMemberBookIdByMemberId(memberRecommendationDto);

        return recommendedMatches.stream()
                .filter(recommendedMatch -> filteredMatches.stream()
                        .noneMatch(filteredMatch -> filteredMatch.getMatchedMemberId().equals(recommendedMatch.getMatchedMemberId())
                                                        && filteredMatch.getMatchedMemberBookId().equals(recommendedMatch.getMatchedMemberBookId())))
                .collect(Collectors.toList());
    }

    /**
     * 필터링 최종 결과
     */
    public List<MatchedInfo> finalFiltering(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto) {
         return matchedInfoRepository.getFinalFilteredMatches(matchingMemberIds,memberRecommendationDto);
    }
}
