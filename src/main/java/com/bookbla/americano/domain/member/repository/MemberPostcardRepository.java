package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberPostcardRepository extends JpaRepository<MemberPostcard, Long> {
    Optional<MemberPostcard> findMemberPostcardByMember_Id(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE MemberPostcard mp SET mp.freePostcardCount = 1")
    void initMemberFreePostcardCount();
}
