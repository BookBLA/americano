package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;

import java.util.List;

public interface MemberMatchingRepositoryCustom {

    List<Long> getMatchingMemberIds(MemberRecommendationDto memberRecommendationDto);

    // 모든 매칭 추천 회원 + 회원 책 정보
    List<MatchedInfo> getAllMatching(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto);

    // 최종 필터링한 매칭 추천 회원 + 회원 책 정보
    List<MatchedInfo> getMatchingInfo(List<Long> matchingMemberIds, MemberRecommendationDto recommendationDto);
}
