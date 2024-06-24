package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PathVariable;

public interface MemberBlockRepository extends JpaRepository<MemberBlock, Long> {

    Optional<MemberBlock> findById(Long memberBlockId);

    Optional<MemberBlock> findByBlockerMemberIdAndBlockedMemberId(@PathVariable("blockerMemberId") Long blockerMemberId, @PathVariable("blockedMemberId") Long blockedMemberId);

    Optional<MemberBlock> findByBlockerMemberAndBlockedMember(Member blockerMember, Member blockedMember);

    List<MemberBlock> findAllByBlockerMember(Member blockerMember);

    List<MemberBlock> findAllByBlockedMember(Member blockedMember);
}
