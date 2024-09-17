package com.bookbla.americano.domain.member.repository;

import java.util.Optional;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBookmarkRepository extends JpaRepository<MemberBookmark, Long> {

    Optional<MemberBookmark> findMemberBookmarkByMemberId(@Param("memberId") Long memberId);

}
