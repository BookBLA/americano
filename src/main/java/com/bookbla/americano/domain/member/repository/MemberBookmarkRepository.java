package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberBookmarkRepository extends JpaRepository<MemberBookmark, Long> {

    Optional<MemberBookmark> findMemberBookmarkByMemberId(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE member_bookmark SET admob_count = :updateCount", nativeQuery = true)
    void resetAdmobCount(@Param("updateCount") int updateCount);

}
