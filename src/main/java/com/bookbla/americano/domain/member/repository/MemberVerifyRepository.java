package com.bookbla.americano.domain.member.repository;

import java.util.Optional;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import com.bookbla.americano.domain.member.exception.MemberVerifyExceptionType;
import com.bookbla.americano.domain.member.repository.custom.MemberVerifyRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberVerifyRepository extends JpaRepository<MemberVerify, Long>, MemberVerifyRepositoryCustom {

    default MemberVerify getByIdOrThrow(Long memberVerifyId) {
        return findById(memberVerifyId)
                .orElseThrow(() -> new BaseException(MemberVerifyExceptionType.ID_NOT_FOUND));
    }

    Page<MemberVerify> findByVerifyTypeAndVerifyStatus(MemberVerifyType verifyType, MemberVerifyStatus verifyStatus, Pageable pageable);

    long countByVerifyTypeAndVerifyStatus(MemberVerifyType verifyType, MemberVerifyStatus verifyStatus);

    Optional<MemberVerify> findFirstByVerifyTypeAndMemberIdOrderByCreatedAtDesc(MemberVerifyType verifyType, Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from MemberVerify mv " +
            "where mv.memberId = :memberId " +
            "and mv.verifyStatus = com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING " +
            "and mv.verifyType = :verifyType")
    void deleteMemberPendingVerifies(Long memberId, MemberVerifyType verifyType);
}
