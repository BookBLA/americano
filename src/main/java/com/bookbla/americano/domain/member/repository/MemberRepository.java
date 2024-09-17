package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import feign.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    default Member getByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    Page<Member> findByMemberStatus(MemberStatus memberStatus, Pageable pageable);

    Optional<Member> findByMemberTypeAndOauthEmail(MemberType memberType, String email);

    Optional<Member> findByMemberProfileSchoolEmail(String schoolEmail);

    Optional<Member> findByMemberProfileName(String name);

    long countByMemberStatus(MemberStatus memberStatus);

    @Query("SELECT m FROM Member m WHERE (m.memberStatus = :status1 OR m.memberStatus = :status2) AND m.memberPolicy.adAgreementPolicy = true")
    List<Member> findByMemberStatusAndAdAgreement(@Param("status1") MemberStatus status1, @Param("status2") MemberStatus status2);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Member m WHERE m.deleteAt <= :cutoffDate AND m.memberStatus = 'DELETED'")
    void deleteAllByDeletedAtBeforeAndMemberStatus(LocalDateTime cutoffDate);

    Optional<Member> findByInvitationCode(String invitationCode);

    @Query(nativeQuery = true,
            value = "select count(m.school_id) " +
                    "from member as m " +
                    "inner join school as s on m.school_id = s.id " +
                    "where s.id = :schoolId " +
                    "and not m.member_status = 'DELETED' " +
                    "and not m.member_status = 'PROFILE' " +
                    "and m.gender = 'FEMALE'")
    long countValidMembers(@Param("schoolId") Long schoolId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE member SET free_bookmark_admob_count = :updateCount, new_person_admob_count = :updateCount", nativeQuery = true)
    void resetAdmobCount(@Param("updateCount") int updateCount);
}
