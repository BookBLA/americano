package com.bookbla.americano.domain.matching.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.repository.entity.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberMatchingRepository extends JpaRepository<MemberMatching, Long>, MemberMatchingRepositoryCustom {

    default MemberMatching getByIdOrThrow(Long memberMatchingId) {
        return findById(memberMatchingId)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MEMBER_MATCHING_NOT_FOUND));
    }

    Optional<MemberMatching> findByMemberId(Long memberId);

    Optional<MemberMatching> findByMember(Member member);

    void deleteByMemberId(Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE member_matching SET is_invitation_card = :isInvitationCard", nativeQuery = true)
    void resetIsInvitationCard(@Param("isInvitationCard") boolean isInvitationCard);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE member_matching SET current_matched_member_id = NULL, current_matched_member_book_id = NULL", nativeQuery = true)
    void resetCurrentMatchedInfo();
}
