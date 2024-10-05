package com.bookbla.americano.domain.matching.repository.custom;

import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;

import java.util.List;

public interface MemberMatchingRepositoryCustom {

    List<Long> getMinimumConstraintMemberIds(MemberRecommendationDto memberRecommendationDto);
}
