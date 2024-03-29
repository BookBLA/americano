package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.postcard.Postcard;
import com.bookbla.americano.domain.postcard.PostcardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    boolean existsBySendMemberIdAndReciveMemberIdAndPostcardStatus(Long sendMemberId, Long reciveMemberId, PostcardStatus status);
}
