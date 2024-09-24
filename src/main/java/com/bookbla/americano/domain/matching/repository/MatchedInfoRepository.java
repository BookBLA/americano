package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchedInfoRepository extends JpaRepository<MatchedInfo, Long> {

    List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId);
}
