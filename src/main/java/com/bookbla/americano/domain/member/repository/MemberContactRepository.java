package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberContact;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberContactRepository extends JpaRepository<MemberContact, Long> {

    List<MemberContact> findByMember(Member member);
}
