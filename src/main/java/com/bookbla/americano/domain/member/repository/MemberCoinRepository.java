package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberCoin;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberCoinRepository extends JpaRepository<MemberCoin, Long> {
    @Query("SELECT mc.coinCount FROM MemberCoin mc WHERE mc.member.id = :memberId")
    Optional<Integer> getMemberCoinByMember_Id(@Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE MemberCoin mc SET mc.coinCount = 2")
    void initMemberCoinCount();
}
