package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.custom.MemberProfileRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository  extends JpaRepository<MemberProfile, Long>, MemberProfileRepositoryCustom {

    Optional<MemberProfile> findByMember(Member member);

}
