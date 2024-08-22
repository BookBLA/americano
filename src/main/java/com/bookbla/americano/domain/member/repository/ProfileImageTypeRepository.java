package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageTypeRepository extends JpaRepository<ProfileImageType, Long> {

    default ProfileImageType getByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new BaseException(MemberExceptionType.PROFILE_TYPE_NOT_FOUND));
    }

}
