package com.bookbla.americano.domain.member.repository;

import java.util.List;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import com.bookbla.americano.domain.member.exception.MemberVerifyExceptionType;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberVerifyRepository extends JpaRepository<MemberVerify, Long> {

    default MemberVerify getByIdOrThrow(Long memberVerifyId) {
        return findById(memberVerifyId)
                .orElseThrow(() -> new BaseException(MemberVerifyExceptionType.ID_NOT_FOUND));
    }

    Page<MemberVerify> findByVerifyTypeAndVerifyStatus(MemberVerifyType verifyType, MemberVerifyStatus verifyStatus, Pageable pageable);

    long countByVerifyTypeAndVerifyStatus(MemberVerifyType verifyType, MemberVerifyStatus verifyStatus);

    @Query("select mv.contents " +
            "from MemberVerify mv " +
            "where mv.memberId = :memberId " +
            "and mv.verifyStatus = com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING " +
            "and mv.verifyType = com.bookbla.americano.domain.member.enums.MemberVerifyType.PROFILE_IMAGE " +
            "order by mv.id desc ")
    List<String> findMemberPendingProfileImage(@Param("memberId") Long memberId);
}
