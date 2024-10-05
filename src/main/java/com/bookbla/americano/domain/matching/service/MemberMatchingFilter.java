package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.repository.MemberBlockRepository;
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
    private final MemberBlockRepository memberBlockRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;
    private final MatchedInfoRepository matchedInfoRepository;

    /**
     * 엽서 거절 조건 필터링
     */
    public List<Long> memberRefusedAtFiltering(Long sendMemberId, List<Long> matchingMemberIds) {

        // 앱 사용자로 부터 받은 엽서를 거절한지 14일이 안된 회원 = 추천되면 안되는 회원
        List<Long> filteredMatchMemberIds = postcardRepository.getReceiveIdsRefusedAt(sendMemberId, matchingMemberIds);

        // matchingMembers - filteredMatchMemberIds => 최종적으로 뽑고싶은 추천 회원 id
        return matchingMemberIds.stream()
                .filter(memberId -> !filteredMatchMemberIds.contains(memberId))
                .collect(Collectors.toList());
    }

    /**
     * 차단된 회원 필터링
     */
    public List<Long> memberBlockedFiltering(Long appMemberId, List<Long> matchingMemberIds) {

        // 앱 사용자로 부터 차단된 회원 = 추천되면 안되는 회원
        List<Long> filteredMatchMemberIds = memberBlockRepository.getBlockedMemberIdsByBlockerMemberId(appMemberId, matchingMemberIds);

        // matchingMembers - filteredMatchMemberIds => 최종적으로 뽑고싶은 추천 회원 id
        return matchingMemberIds.stream()
                .filter(memberId -> !filteredMatchMemberIds.contains(memberId))
                .collect(Collectors.toList());
    }

    /**
     * 무시한 회원 필터링
     */
    public List<MatchedInfo> memberIgnoredFiltering(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto) {
        List<MatchedInfo> matches = matchedInfoRepository.getAllMatches(matchingMemberIds, memberRecommendationDto);
        List<MatchedInfo> filteredMatches = matchIgnoredRepository.getIgnoredMemberIdsAndIgnoredMemberBookIdByMemberId(matchingMemberIds, memberRecommendationDto);

        return matches.stream()
                .filter(match -> filteredMatches.stream()
                        .noneMatch(filteredMatch -> filteredMatch.getMatchedMemberId().equals(match.getMatchedMemberId())))
                .collect(Collectors.toList());

    }

    /**
     * 필터링 최종 결과
     */
    public List<MatchedInfo> finalFiltering(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto) {
         return matchedInfoRepository.getFinalFilteredMatches(matchingMemberIds,memberRecommendationDto);
    }
}
