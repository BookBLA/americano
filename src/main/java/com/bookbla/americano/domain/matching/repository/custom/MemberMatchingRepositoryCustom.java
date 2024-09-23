package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;

import java.util.List;

public interface MemberMatchingRepositoryCustom {

    List<MatchedInfo> getMatchingMembers(MemberRecommendationDto memberRecommendationDto);
}
