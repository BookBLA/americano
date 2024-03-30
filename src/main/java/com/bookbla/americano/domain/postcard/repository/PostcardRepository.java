package com.bookbla.americano.domain.postcard.repository;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostcardRepository extends JpaRepository<Postcard, Long> {
    boolean existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(Long sendMemberId, Long receiveMemberId, PostcardStatus status);
}
