package com.bookbla.americano.domain.postcard.repository;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostcardRepository extends JpaRepository<Postcard, Long>, PostcardRepositoryCustom {
    boolean existsBySendMemberIdAndReceiveMemberIdAndPostcardStatus(Long sendMemberId, Long receiveMemberId, PostcardStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MemberPostcard mp set mp.freePostcardCount = mp.freePostcardCount - 1 where mp.member.id = :memberId")
    void useMemberFreePostcard(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MemberPostcard mp set mp.payPostcardCount = mp.payPostcardCount - 1 where mp.member.id = :memberId")
    void useMemberPayPostcard(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Postcard p set p.postcardStatus = :status where p.id = :postcardId")
    void updatePostcardStatus(@Param("status") PostcardStatus status, @Param("postcardId") Long postcardId);
}
