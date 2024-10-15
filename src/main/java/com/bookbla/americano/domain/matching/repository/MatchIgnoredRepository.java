package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.domain.matching.repository.custom.MatchIgnoredRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchIgnoredInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchIgnoredRepository extends JpaRepository<MatchIgnoredInfo, Long>, MatchIgnoredRepositoryCustom {

    Optional<MatchIgnoredInfo> findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(Long memberId, Long ignoredMemberId, Long ignoredMemberBookId);
}
