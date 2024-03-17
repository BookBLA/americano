package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    @Override
    @Transactional
    public MemberProfileResponse createMemberProfile(Long memberId, MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.save(memberProfileDto.toEntity(member));

        // 프로필 정보 입력이 완료되면 가입 승인 상태로 변경
        member.updateMemberStatus(MemberStatus.APPROVAL);

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse readMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Override
    @Transactional
    public MemberProfileResponse updateMemberProfile(Long memberId,
        MemberProfileUpdateRequest memberProfileUpdateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);

        update(memberProfile, memberProfileUpdateRequest);

        return MemberProfileResponse.from(member, memberProfile);
    }

    public void update(MemberProfile memberProfile, MemberProfileUpdateRequest request) {
        memberProfile.updateName(request.getName())
            .updateBirthDate(request.getBirthDate())
            .updateSchoolName(request.getSchoolName())
            .updateGender(request.getGender())
            .updateProfileImageUrl(request.getProfileImageUrl())
            .updateOpenKakaoRoomUrl(request.getOpenKakaoRoomUrl());
    }

}
