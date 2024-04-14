package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEmailRepository extends JpaRepository<MemberEmail, Long> {

    Optional<MemberEmail> findBySchoolEmail(String SchoolEmail);

    Optional<MemberEmail> findByMember(Member member);
}

