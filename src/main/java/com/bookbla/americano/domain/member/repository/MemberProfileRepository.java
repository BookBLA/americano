package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberProfileException;
import com.bookbla.americano.domain.member.repository.custom.MemberProfileRepositoryCustom;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository  extends JpaRepository<MemberProfile, Long>, MemberProfileRepositoryCustom {

    default MemberProfile getByMemberOrThrow(Member member) {
        return findByMember(member)
            .orElseThrow(() -> new BaseException(MemberExceptionType.PROFILE_NOT_REGISTERED));
    }

    Optional<MemberProfile> findByMember(Member member);

    List<MemberProfile> findByOpenKakaoRoomStatus(OpenKakaoRoomStatus openKakaoRoomStatus, Pageable pageable);

    List<MemberProfile> findByProfileImageStatus(ProfileImageStatus profileImageStatus, Pageable pageable);

    List<MemberProfile> findByStudentIdImageStatus(StudentIdImageStatus studentIdImageStatus, Pageable pageable);

    default MemberProfile getByIdOrThrow(Long memberProfileId) {
        return findById(memberProfileId).orElseThrow(() -> new BaseException(MemberProfileException.NOT_FOUND_ID));
    }

    Optional<MemberProfile> findById(Long memberProfileId);

}
