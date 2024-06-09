package com.bookbla.americano.domain.memberask.repository;

import java.util.Optional;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAskRepository extends JpaRepository<MemberAsk, Long> {

    Optional<MemberAsk> findByMember(Member member);
}
