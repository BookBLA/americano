package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {

    default MemberAuth getByMemberOrThrow(Member member) {
        return findByMember(member)
            .orElseThrow(() -> new BaseException(MemberExceptionType.AUTH_NOT_REGISTERED));
    }

    Optional<MemberAuth> findByMember(Member member);

    Optional<MemberAuth> findBySchoolEmail(String schoolEmail);

}