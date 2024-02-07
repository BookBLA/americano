package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    Optional<Member> findByMemberTypeAndOauthEmail(MemberType memberType, String email);
}
