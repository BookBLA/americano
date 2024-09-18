package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.MemberMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MemberMatchRepository extends JpaRepository<MemberMatch, Long> {

    @Query(value = "SELECT DISTINCT m.name, mb.book_id " +
            "FROM member_book mb " +
            "JOIN member m ON m.id = mb.member_id " +
            "WHERE m.id <> :memberId " +
            "AND m.gender <> :gender " +
            "AND COALESCE(m.last_used_at, '1900-01-01') > :twoWeeksAgo",
            nativeQuery = true)
    List<Map<Long, Long>> findDistinctMemberBooks(
            @Param("memberId") Long memberId,
            @Param("gender") Gender gender,
            @Param("date") LocalDateTime twoWeeksAgo);
}