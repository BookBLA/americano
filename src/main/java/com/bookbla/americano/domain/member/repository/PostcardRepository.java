package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.postcard.Postcard;
import com.bookbla.americano.domain.postcard.PostcardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    Optional<Postcard> findBySendMemberIdAndReciveMemberIdAndPostcardStatus(Long sendMemberId, Long reciveMemberId, PostcardStatus status);

    Optional<Postcard> findByReciveMemberIdAndSendMemberIdAndPostcardStatus(Long reciveMemberId, Long sendMemberId, PostcardStatus status);
}
