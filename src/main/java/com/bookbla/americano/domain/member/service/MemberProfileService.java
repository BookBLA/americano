package com.bookbla.americano.domain.member.service;

import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.sendbird.service.SendbirdService;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileStudentIdImageUrlUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.*;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MemberVerifyRepository memberVerifyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SendbirdService sendbirdService;

    @Transactional
    public MemberProfileResponse createMemberProfile(Long memberId,
                                                     MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseThrow(
                        () -> new BaseException(MemberEmailExceptionType.EMAIL_NOT_REGISTERED));
        memberEmail.validatePending();
        validateDuplicateName(memberProfileDto.getName());
        validateDuplicateSchoolEmail(memberProfileDto.getSchoolEmail());

        MemberStatus beforeStatus = member.getMemberStatus();

        MemberProfile profile = memberProfileDto.toMemberProfile();
        member.updateMemberProfile(profile)
                .updateMemberStatus(MemberStatus.STYLE, LocalDateTime.now());

        // member 객체 명시적으로 save 선언
        memberRepository.save(member);
        memberStatusLogRepository.save(
                MemberStatusLog.builder()
                        .memberId(member.getId())
                        .beforeStatus(beforeStatus)
                        .afterStatus(MemberStatus.STYLE)
                        .build()
        );

        return MemberProfileResponse.from(member, profile);
    }

    public MemberNameVerifyResponse verifyMemberName(String name) {

        validateDuplicateName(name);
        if (!MemberProfile.verifyNickname(name)) {
            throw new BaseException(MemberProfileExceptionType.CONTAIN_BAD_WORDS);
        }

        return MemberNameVerifyResponse.of(true);
    }

    private void validateDuplicateName(String name) {
        memberRepository.findByMemberProfileName(name)
                .ifPresent(profile -> {
                    throw new BaseException(MemberProfileExceptionType.ALREADY_EXISTS_NICKNAME);
                });
    }

    private void validateDuplicateSchoolEmail(String schoolEmail) {
        memberRepository.findByMemberProfileSchoolEmail(schoolEmail)
            .ifPresent(profile -> {
                throw new BaseException(MemberEmailExceptionType.ALREADY_EXIST);
            });
    }

    private void saveStudentIdVerify(Member member,
                                     String studentImageUrl) {

        memberVerifyRepository.deleteMemberPendingVerifies(member.getId(), STUDENT_ID);
        MemberVerify studentIdVerify = MemberVerify.builder()
                .memberId(member.getId())
                .description(toMemberVerifyDescription(member))
                .contents(studentImageUrl)
                .verifyType(STUDENT_ID)
                .build();
        eventPublisher.publishEvent(new AdminNotificationEvent(STUDENT_ID.name(), member.getId().toString()));
        memberVerifyRepository.save(studentIdVerify);
    }

    @Transactional(readOnly = true)
    public MemberProfileResponse readMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Transactional
    public MemberProfileResponse updateMemberProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        validateDuplicateName(request.getName());

        update(member, memberProfile, request);
        sendbirdService.updateSendbirdNickname(memberId, request.getName());

        return MemberProfileResponse.from(member, member.getMemberProfile());
    }

    private void update(Member member, MemberProfile memberProfile, MemberProfileUpdateRequest request) {
        member.updateMemberProfile(memberProfile);
        member.getMemberProfile().updateName(request.getName())
                .updateBirthDate(request.getBirthDate())
                .updateGender(request.getGender())
                .updateSchoolEmail(request.getSchoolEmail());
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberProfileStatusResponse readMemberProfileStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();
        return MemberProfileStatusResponse.from(memberProfile);
    }

    @Transactional(readOnly = true)
    public List<MemberBookProfileResponse> getAllMembers(Long memberId,
                                                         MemberBookProfileRequestDto requestDto) {
        List<BookProfileResponse> allResult = memberRepository.getAllMembers(memberId, requestDto);
        // 탐색 결과가 없을 때, EMPTY_MEMBER_BOOK Exception(해당 사용자의 선호 책도 없음)
        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }

        return allResult.stream()
                .map(MemberBookProfileResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMemberProfileStudentId(Long memberId,
        MemberProfileStudentIdImageUrlUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        if (memberProfile.getStudentIdImageUrl() == null || !memberProfile.getStudentIdImageUrl()
            .equals(request.getStudentIdImageUrl())) {
            saveStudentIdVerify(member, request.getStudentIdImageUrl());
            member.updateMemberStatus(MemberStatus.APPROVAL, LocalDateTime.now());
            memberProfile.updateStudentIdImageStatus(StudentIdImageStatus.PENDING);
            memberRepository.save(member);
        }
    }

    public MemberProfileStudentIdReadResponse readMemberProfileStudentIdStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();
        return MemberProfileStudentIdReadResponse.from(memberProfile);
    }

    private String toMemberVerifyDescription(Member member) {
        return "name: " + member.getMemberProfile().getName() +
            ", schoolName: " + member.getSchool().getName() +
            ", birthDate: " + member.getMemberProfile().getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE) +
            ", gender: " + member.getMemberProfile().getGenderName();
    }
}
