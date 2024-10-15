package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberOnboardingStatusResponse;
import com.bookbla.americano.domain.member.enums.OnboardingType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberModal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberModalService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberOnboardingStatusResponse getMemberOnboarding(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberModal modal = member.getMemberModal();

        return MemberOnboardingStatusResponse.builder()
                .homeOnboardingStatus(modal.getHomeOnboarding())
                .libraryOnboardingStatus(modal.getLibraryOnboarding())
                .build();
    }

    @Transactional
    public MemberOnboardingStatusResponse updateMemberOnboarding(Long memberId, OnboardingType onboarding) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        MemberModal modal = member.getMemberModal();
        updateMemberOnboarding(modal, onboarding);

        return MemberOnboardingStatusResponse.from(modal.getHomeOnboarding(), modal.getLibraryOnboarding());
    }

    private void updateMemberOnboarding(MemberModal modal, OnboardingType onboarding) {
        if (onboarding.equals(OnboardingType.HOME)) {
            modal.updateMemberHomeOnboarding();
        } else if (onboarding.equals(OnboardingType.LIBRARY)) {
            modal.updateMemberLibraryOnboarding();
        } else {
            throw new BaseException(MemberExceptionType.ONBOARDING_NOT_VALID);
        }
    }
}