package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {

    default MemberProfile getByMemberOrThrow(Member member) {
        return findByMember(member)
            .orElseThrow(() -> new BaseException(MemberExceptionType.PROFILE_NOT_REGISTERED));
    }

    Optional<MemberProfile> findByMember(Member member);

}