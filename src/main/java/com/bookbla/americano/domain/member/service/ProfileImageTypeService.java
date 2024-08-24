package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.domain.member.controller.dto.response.ProfileImageTypeReadResponse;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class ProfileImageTypeService {

    private final MemberRepository memberRepository;
    private final ProfileImageTypeRepository profileImageTypeRepository;

    @Transactional(readOnly = true)
    public ProfileImageTypeReadResponse readAll() {
        List<ProfileImageType> profileImageTypes = profileImageTypeRepository.findAll();
        return ProfileImageTypeReadResponse.from(profileImageTypes);
    }

    @Transactional(readOnly = true)
    public ProfileImageTypeReadResponse readMemberGenderProfileImageTypes(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Gender gender = member.getMemberProfile().getGender();
        List<ProfileImageType> profileImageTypes = profileImageTypeRepository.findAllByGender(gender);
        return ProfileImageTypeReadResponse.from(profileImageTypes);
    }
}
