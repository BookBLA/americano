package com.bookbla.americano.domain.postcard.repository;

import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    boolean existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(Long sendMemberId, Long receiveMemberId, PostcardStatus status);
}
