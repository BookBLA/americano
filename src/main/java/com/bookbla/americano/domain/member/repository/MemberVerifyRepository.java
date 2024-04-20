package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import com.bookbla.americano.domain.member.exception.MemberVerifyExceptionType;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberVerifyRepository extends JpaRepository<MemberVerify, Long> {

    Page<MemberVerify> findByTypeAndStatus(MemberVerifyType type, MemberVerifyStatus status, Pageable pageable);

    default MemberVerify getByIdOrThrow(Long memberVerifyId) {
        return findById(memberVerifyId)
                .orElseThrow(() -> new BaseException(MemberVerifyExceptionType.ID_NOT_FOUND));
    }
}
