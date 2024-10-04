package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;

import java.util.List;

public interface MatchIgnoredRepositoryCustom {

    List<MatchedInfo> getIgnoredMemberIdsAndIgnoredMemberBookIdByMemberId(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto);
}
