package com.bookbla.americano.domain.member.repository;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    default Member getByIdOrThrow(Long memberId) {
        return findById(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.MEMBER_NOT_FOUND));
    }

    Optional<Member> findByMemberTypeAndOauthEmail(MemberType memberType, String email);

    Optional<Member> findByMemberProfileSchoolEmail(String schoolEmail);

    Page<Member> findByMemberProfileOpenKakaoRoomStatus(OpenKakaoRoomStatus openKakaoRoomStatus, Pageable pageable);

    Page<Member> findByMemberProfileProfileImageStatus(ProfileImageStatus profileImageStatus, Pageable pageable);

    Page<Member> findByMemberProfileStudentIdImageStatus(StudentIdImageStatus studentIdImageStatus, Pageable pageable);
}
