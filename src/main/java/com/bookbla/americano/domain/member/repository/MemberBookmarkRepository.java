package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberBookmarkRepository extends JpaRepository<MemberBookmark, Long> {
    Optional<MemberBookmark> findMemberBookmarkByMemberId(@Param("memberId") Long memberId);
}
