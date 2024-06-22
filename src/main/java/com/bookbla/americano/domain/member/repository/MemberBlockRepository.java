package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long> {

    Optional<MemberBlock> findById(Long memberBlockId);

    Optional<MemberBlock> findByBlockerMemberAndBlockedMember(Member blockerMember, Member blockedMember);

    List<MemberBlock> findAllByBlockerMember(Member blockerMember);

    List<MemberBlock> findAllByBlockedMember(Member blockedMember);
}
