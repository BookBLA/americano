package com.bookbla.americano.domain.postcard.repository;

import java.util.List;
import java.util.Optional;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostcardRepository extends JpaRepository<Postcard, Long>, PostcardRepositoryCustom {

    boolean existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(Long sendMemberId, Long receiveMemberId, PostcardStatus status);

    @Query("SELECT p FROM Postcard p JOIN FETCH p.receiveMember JOIN FETCH p.sendMember WHERE p.id = :postcardId")
    Optional<Postcard> findByIdWithMembers(@Param("postcardId") Long postcardId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Postcard p set p.postcardStatus = :status where p.id = :postcardId")
    void updatePostcardStatus(@Param("status") PostcardStatus status, @Param("postcardId") Long postcardId);

    List<Postcard> findBySendMemberIdAndReceiveMemberId(@Param("sendMemberId") Long sendMemberId, @Param("receiveMemberId") Long receiveMemberId);
}
