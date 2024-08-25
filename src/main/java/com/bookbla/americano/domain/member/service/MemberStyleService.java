package com.bookbla.americano.domain.member.service;


import java.time.LocalDateTime;

import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberStatus.BOOK;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberStyleService {

    private final ProfileImageTypeRepository profileImageTypeRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;

    @Transactional(readOnly = true)
    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = member.getMemberStyle();

        return MemberStyleResponse.of(member, memberStyle);
    }

    public MemberStyleResponse createMemberStyle(Long memberId,
                                                 MemberStyleCreateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.validateStyleRegistered();

        ProfileImageType profileImageType = profileImageTypeRepository.getByIdOrThrow(request.getProfileImageTypeId());

        member.updateMemberStyle(request.toMemberStyle(profileImageType));

        MemberStatus beforeStatus = member.getMemberStatus();
        member.updateMemberStatus(BOOK, LocalDateTime.now());
        memberRepository.save(member);

        memberStatusLogRepository.save(
                MemberStatusLog.builder()
                        .memberId(member.getId())
                        .beforeStatus(beforeStatus)
                        .afterStatus(BOOK)
                        .build()
        );
        return MemberStyleResponse.of(member, member.getMemberStyle());
    }

    public void updateMemberStyle(Long memberId,
                                  MemberStyleUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = member.getMemberStyle();

        ProfileImageType profileImageType = profileImageTypeRepository.getByIdOrThrow(request.getProfileImageTypeId());

        memberStyle.updateMbti(request.getMbti())
                .updateSmokeType(request.getSmokeType())
                .updateHeight(request.getHeight())
                .updateProfileImageType(profileImageType);
    }

    public void updateMemberProfileImageType(Long memberId, Long profileImageTypeId) {
        ProfileImageType profileImageType = profileImageTypeRepository.getByIdOrThrow(profileImageTypeId);
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = member.getMemberStyle();

        member.updateMemberStyle(MemberStyle.builder()
                .profileImageType(profileImageType)
                .height(memberStyle.getHeight())
                .mbti(memberStyle.getMbti())
                .smokeType(memberStyle.getSmokeType())
                .build());
    }
}
