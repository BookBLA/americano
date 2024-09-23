package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchedInfoRepository extends JpaRepository<MatchedInfo, Long> {
}
