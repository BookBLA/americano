package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import feign.Param;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberEmailRepository extends JpaRepository<MemberEmail, Long> {

    Optional<MemberEmail> findByMember(Member member);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE MemberEmail me WHERE me.createdAt <= :twoDaysAgo")
    void deleteMemberEmailSchedule(@Param("twoDaysAgo") LocalDateTime twoDaysAgo);
}

