package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;

import java.util.List;
import java.util.Map;

public interface MemberMatchingRepositoryCustom {

    List<Map<Long, Long>> getMatchingMemberList(MemberRecommendationDto memberRecommendationDto);
}
