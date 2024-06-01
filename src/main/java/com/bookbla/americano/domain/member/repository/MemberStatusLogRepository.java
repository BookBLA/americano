package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStatusLogRepository extends JpaRepository<MemberStatusLog, Long> {

    default MemberStatusLog getByMemberIdOrThrow(Long memberId) {
        return findByMemberId(memberId)
            .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_STATUS_LOG_NOT_FOUND));
    }

    Optional<MemberStatusLog> findByMemberId(Long memberId);
}
