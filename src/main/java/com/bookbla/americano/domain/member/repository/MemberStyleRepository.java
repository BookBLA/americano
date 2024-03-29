package com.bookbla.americano.domain.member.repository;

import java.util.Optional;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberStyleRepository extends JpaRepository<MemberStyle, Long> {

    Optional<MemberStyle> findByMember(Member member);
}
