package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.postcard.Postcard;
import com.bookbla.americano.domain.postcard.PostcardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    Boolean existsBySendMemberIdAndReciveMemberIdAndPostcardStatus(Long sendMemberId, Long reciveMemberId, PostcardStatus status);
    Boolean existsByReciveMemberIdAndSendMemberIdAndPostcardStatus(Long reciveMemberId, Long sendMemberId, PostcardStatus status);
}
