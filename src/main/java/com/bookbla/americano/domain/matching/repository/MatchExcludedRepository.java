package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchExcludedRepository extends JpaRepository<MatchExcludedInfo, Long> {

    Optional<MatchExcludedInfo> findByMemberIdAndExcludedMemberId(Long memberId, Long excludedMemberId);
}
