package com.bookbla.americano.domain.matching.repository.custom;

import java.util.List;

public interface MatchExcludedRepositoryCustom {

    List<Long> getExcludedMemberIdsByMemberId(Long memberId, List<Long> matchingMemberIds);
}
