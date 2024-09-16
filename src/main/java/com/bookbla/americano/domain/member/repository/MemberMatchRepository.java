package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberMatchRepository extends JpaRepository<MemberMatch, Long> {
}