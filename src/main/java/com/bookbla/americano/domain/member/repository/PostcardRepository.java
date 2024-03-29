package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.postcard.Postcard;
import com.bookbla.americano.domain.postcard.PostcardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    boolean existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(Long sendMemberId, Long receiveMemberId, PostcardStatus status);
}
