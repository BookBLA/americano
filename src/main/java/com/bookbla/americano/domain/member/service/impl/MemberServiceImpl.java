package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.auth.repository.MemberSignUpInformationRepository;
import com.bookbla.americano.domain.auth.repository.entity.MemberSignUpInformation;
import com.bookbla.americano.domain.member.controller.dto.request.MemberUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberSignUpInformationRepository memberSignUpInformationRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public MemberResponse createMember(
            Long memberSignUpInformationId,
            MemberProfileDto memberProfileDto
    ) {
        MemberSignUpInformation memberSignUpInformation = memberSignUpInformationRepository.findById(memberSignUpInformationId).orElseThrow();

        MemberProfile memberProfile = memberProfileDto.toEntity();

        memberProfile.updateOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING)
                .updateStudentIdImageStatus(StudentIdImageStatus.PENDING)
                .updateProfileImageStatus(ProfileImageStatus.PENDING);

        Member member = Member.builder()
                .oauthEmail(memberSignUpInformation.getEmail())
                .memberType(memberSignUpInformation.getMemberType())
                .memberProfile(memberProfile)
                .memberStatus(MemberStatus.APPROVAL)
                .build();

        Member savedMember = memberRepository.save(member);
        memberSignUpInformationRepository.deleteById(memberSignUpInformationId);
        return MemberResponse.from(savedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberStatusResponse readMemberStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberStatusResponse.from(member);
    }

    @Override
    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        update(member, memberUpdateRequest);

        return MemberResponse.from(member);
    }

    private void update(Member member, MemberUpdateRequest request) {
        member.updateOauthEmail(request.getOauthEmail())
                .updateMemberType(request.getMemberType())
                .checkMemberStatus(request.getMemberStatus())
                .updateMemberType(request.getMemberType());
    }
}