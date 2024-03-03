package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    @Override
    public void createMemberProfile(Long memberId, MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        memberProfileRepository.save(memberProfileDto.toEntity(member));
    }

    @Override
    public MemberProfileResponse readMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Override
    public MemberProfileResponse updateMemberProfile(Long memberId,
        MemberProfileUpdateRequest memberProfileUpdateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.getByMemberOrThrow(member);

        update(memberProfile, memberProfileUpdateRequest);

        return MemberProfileResponse.from(member, memberProfile);
    }

    public void update(MemberProfile memberProfile,
        MemberProfileUpdateRequest memberProfileUpdateRequest) {
        memberProfile.updateName(memberProfileUpdateRequest.getName());
        memberProfile.updateBirthDate(memberProfileUpdateRequest.getBirthDate());
        memberProfile.updateSchoolName(memberProfileUpdateRequest.getSchoolName());
        memberProfile.updateGender(memberProfileUpdateRequest.getGender());
        memberProfile.updateOpenKakaoRoomUrl(memberProfileUpdateRequest.getOpenKakaoRoomUrl());

    }

}