package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.MemberStyle;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStyleRepository extends JpaRepository<MemberStyle, Long> {

    default MemberStyle getByMemberOrThrow(Member member) {
        return findByMember(member)
                .orElseThrow(() -> new BaseException(MemberExceptionType.STYLE_NOT_REGISTERED));
    }

    Optional<MemberStyle> findByMember(Member member);
}
