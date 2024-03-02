package com.bookbla.americano.domain.member;

import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByOauthEmailAndMemberType(String email, MemberType memberType);
}
