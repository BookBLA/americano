package com.bookbla.americano.domain.memberask.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAskRepository extends JpaRepository<MemberAsk, Long> {

    default MemberAsk getByMemberOrThrow(Member member) {
        return findByMember(member)
                .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));
    }

    Optional<MemberAsk> findByMember(Member member);
}
