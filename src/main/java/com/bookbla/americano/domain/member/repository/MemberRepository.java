package com.bookbla.americano.domain.member.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    default Member getByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    Page<Member> findByMemberStatus(MemberStatus memberStatus, Pageable pageable);

    Optional<Member> findByMemberTypeAndOauthEmail(MemberType memberType, String email);

    Optional<Member> findByMemberProfileSchoolEmail(String schoolEmail);

    long countByMemberStatus(MemberStatus memberStatus);

    @Query("SELECT m FROM Member m WHERE m.memberStatus = :status1 OR m.memberStatus = :status2")
    List<Member> findByMemberStatus(@Param("status1") MemberStatus status1, @Param("status2") MemberStatus status2);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Member m WHERE m.deleteAt <= :cutoffDate AND m.memberStatus = 'DELETED'")
    void deleteAllByDeletedAtBeforeAndMemberStatus(LocalDateTime cutoffDate);

    List<Member> findByMemberPolicyAdAgreementPolicy(boolean b);
}
