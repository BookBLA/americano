package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.policy.Policy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPolicyRepository extends JpaRepository<MemberPolicy, Long> {

    Optional<MemberPolicy> findByMemberAndPolicy(Member member, Policy policy);

    List<MemberPolicy> findAllByMember(Member member);
}
