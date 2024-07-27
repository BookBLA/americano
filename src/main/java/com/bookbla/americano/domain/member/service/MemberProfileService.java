package com.bookbla.americano.domain.member.service;

import static com.bookbla.americano.domain.member.enums.MemberVerifyType.OPEN_KAKAO_ROOM_URL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.PROFILE_IMAGE;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileImageUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileOpenKakaoRoomUrlUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.ProfileModifyRequest;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    @Transactional
    public MemberProfileResponse createMemberProfile(Long memberId,
                                                     MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseThrow(
                        () -> new BaseException(MemberEmailExceptionType.EMAIL_NOT_REGISTERED));
        memberEmail.validatePending();
        validateDuplicatedNickname(memberProfileDto.getName());

        saveProfileImageVerify(member, memberProfileDto.getProfileImageUrl());
        saveKakaoRoomVerify(member, memberProfileDto.getOpenKakaoRoomUrl());
        saveStudentIdVerify(member, memberProfileDto.toMemberVerifyDescription(),
                memberProfileDto.getStudentIdImageUrl());

        MemberStatus beforeStatus = member.getMemberStatus();
        MemberProfile memberProfile = memberProfileDto.toMemberProfile();
        member.updateMemberProfile(memberProfile)
                .updateMemberStatus(MemberStatus.STYLE, LocalDateTime.now());

        // member 객체 명시적으로 save 선언
        memberRepository.save(member);
        memberStatusLogRepository.save(
                MemberStatusLog.builder()
                        .memberId(member.getId())
                        .beforeStatus(beforeStatus)
                        .afterStatus(MemberStatus.APPROVAL)
                        .build()
        );

        return MemberProfileResponse.from(member, memberProfile);
    }

    private void saveStudentIdVerify(Member member, String verifyDescription,
                                     String studentImageUrl) {
        memberVerifyRepository.deleteMemberPendingVerifies(member.getId(), STUDENT_ID);
        MemberVerify studentIdVerify = MemberVerify.builder()
                .memberId(member.getId())
                .description(verifyDescription)
                .contents(studentImageUrl)
                .verifyType(STUDENT_ID)
                .build();
        eventPublisher.publishEvent(new AdminNotificationEvent(STUDENT_ID.name(), member.getId().toString()));
        memberVerifyRepository.save(studentIdVerify);
    }

    private void saveProfileImageVerify(Member member, String imageUrl) {
        memberVerifyRepository.deleteMemberPendingVerifies(member.getId(), PROFILE_IMAGE);
        MemberVerify profileImageVerify = MemberVerify.builder()
                .memberId(member.getId())
                .contents(imageUrl)
                .verifyType(PROFILE_IMAGE)
                .build();
        memberVerifyRepository.save(profileImageVerify);
        eventPublisher.publishEvent(new AdminNotificationEvent(PROFILE_IMAGE.name(), member.getId().toString()));
    }

    private void saveKakaoRoomVerify(Member member, String kakaoRoomUrl) {
        memberVerifyRepository.deleteMemberPendingVerifies(member.getId(), OPEN_KAKAO_ROOM_URL);
        MemberVerify kakaoRoomVerify = MemberVerify.builder()
                .memberId(member.getId())
                .contents(kakaoRoomUrl)
                .verifyType(OPEN_KAKAO_ROOM_URL)
                .build();
        memberVerifyRepository.save(kakaoRoomVerify);
        eventPublisher.publishEvent(new AdminNotificationEvent(OPEN_KAKAO_ROOM_URL.name(), member.getId().toString()));
    }

    @Transactional(readOnly = true)
    public MemberProfileResponse readMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Transactional
    public void modifyProfile(Long memberId, ProfileModifyRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        if (!memberProfile.getOpenKakaoRoomUrl().equals(request.getOpenKakaoRoomUrl())) {
            saveKakaoRoomVerify(member, request.getOpenKakaoRoomUrl());
        }

        validateDuplicatedNickname(request.getName());

        memberProfile.updateName(request.getName())
                .updatePhoneNumber(request.getPhoneNumber())
                .updateOpenKakaoRoomUrl(request.getOpenKakaoRoomUrl());
//                .updateOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING);

        member.updateMemberProfile(memberProfile);
        memberRepository.save(member);
    }

    @Transactional
    public MemberProfileResponse updateMemberProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        if (!memberProfile.getOpenKakaoRoomUrl().equals(request.getOpenKakaoRoomUrl())) {
            saveKakaoRoomVerify(member, request.getOpenKakaoRoomUrl());
            memberProfile.updateOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING);
        }

        if (!memberProfile.getProfileImageUrl().equals(request.getProfileImageUrl())) {
            saveProfileImageVerify(member, request.getProfileImageUrl());
            memberProfile.updateProfileImageStatus(ProfileImageStatus.PENDING);
        }

        if (!memberProfile.getStudentIdImageUrl().equals(request.getStudentIdImageUrl())) {
            saveStudentIdVerify(member, request.toMemberVerifyDescription(), request.getStudentIdImageUrl());
            memberProfile.updateStudentIdImageStatus(StudentIdImageStatus.PENDING);
        }

        validateDuplicatedNickname(request.getName());

        update(member, memberProfile, request);

        return MemberProfileResponse.from(member, member.getMemberProfile());
    }

    private void update(Member member, MemberProfile memberProfile, MemberProfileUpdateRequest request) {
        member.updateMemberProfile(memberProfile);
        member.getMemberProfile().updateName(request.getName())
                .updateBirthDate(request.getBirthDate())
                .updateGender(request.getGender())
                .updateSchoolEmail(request.getSchoolEmail())
                .updatePhoneNumber(request.getPhoneNumber());
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public MemberProfileStatusResponse readMemberProfileStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();
        return MemberProfileStatusResponse.from(memberProfile);
    }

    public void updateMemberProfileImage(
            Long memberId, MemberProfileImageUpdateRequest request
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        saveProfileImageVerify(member, request.getProfileImageUrl());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateProfileImageUrl(request.getProfileImageUrl());
//        memberProfile.updateProfileImageStatus(ProfileImageStatus.PENDING);
        memberRepository.save(member);
    }

    public void updateMemberProfileKakaoRoom(
            Long memberId, MemberProfileOpenKakaoRoomUrlUpdateRequest request
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        saveKakaoRoomVerify(member, request.getOpenKakaoRoomUrl());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateOpenKakaoRoomUrl(request.getOpenKakaoRoomUrl());
//        memberProfile.updateOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING);
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<MemberBookProfileResponse> findSameBookMembers(Long memberId,
                                                               MemberBookProfileRequestDto requestDto) {
        List<MemberBookProfileResponse> allResult = memberRepository.searchSameBookMember(
                memberId, requestDto);
        // 탐색 결과가 없을 때, EMPTY_MEMBER_BOOK Exception(해당 사용자의 선호 책도 없음)
        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }
        // 해당 사용자의 선호 책
        List<MemberBookProfileResponse> userBookProfiles = new ArrayList<>();
        List<MemberBookProfileResponse> otherBookProfiles = new ArrayList<>(allResult);
        for (MemberBookProfileResponse i : allResult) {
            if (i.getMemberId() == memberId) {
                addBookProfileToList(userBookProfiles, i);
                otherBookProfiles.remove(i);
            }
        }
        // 해당 사용자의 선호책이 없을 때, EMPTY_MEMBER_BOOK Exception
        if (userBookProfiles.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }

        // 1순위 : 대표책 - 대표책
        List<MemberBookProfileResponse> firstResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                MemberBookProfileResponse::isBookIsRepresentative,
                MemberBookProfileResponse::isBookIsRepresentative
        );

        Collections.shuffle(firstResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, firstResults);

        // 2순위 : 대표책 - 선호책
        List<MemberBookProfileResponse> secondResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                MemberBookProfileResponse::isBookIsRepresentative,
                otherBookProfile -> !otherBookProfile.isBookIsRepresentative()
        );

        Collections.shuffle(secondResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, secondResults);

        // 3순위 : 선호책 - 대표책
        List<MemberBookProfileResponse> thirdResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                userBookProfile -> !userBookProfile.isBookIsRepresentative(),
                MemberBookProfileResponse::isBookIsRepresentative
        );

        Collections.shuffle(thirdResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, thirdResults);

        // 4순위 : 선호책 - 선호책
        List<MemberBookProfileResponse> fourthResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                userBookProfile -> !userBookProfile.isBookIsRepresentative(),
                otherBookProfile -> !otherBookProfile.isBookIsRepresentative()
        );

        Collections.shuffle(fourthResults);

        return Stream.of(firstResults, secondResults, thirdResults, fourthResults)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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

    private List<MemberBookProfileResponse> findMatches(
            List<MemberBookProfileResponse> userBookProfiles,
            List<MemberBookProfileResponse> otherBookProfiles,
            Predicate<MemberBookProfileResponse> userPredicate,
            Predicate<MemberBookProfileResponse> otherPredicate) {

        return new ArrayList<>(userBookProfiles.stream()
                .filter(userPredicate)
                .flatMap(userBookProfile -> otherBookProfiles.stream()
                        .filter(otherBookProfile -> otherBookProfile.getBookId()
                                == userBookProfile.getBookId())
                        .filter(otherPredicate)
                ).collect(Collectors.groupingBy(MemberBookProfileResponse::getMemberId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                memberBookProfileList -> memberBookProfileList.get(0)
                        )))
                .values());
    }

    private void removeMemberToOtherBookProfiles(
            List<MemberBookProfileResponse> otherBookProfiles,
            List<MemberBookProfileResponse> resultBookProfiles) {
        otherBookProfiles.removeIf(otherBookProfile -> resultBookProfiles.stream()
                .anyMatch(resultBookProfile -> resultBookProfile.getMemberId()
                        == otherBookProfile.getMemberId()));
    }

    private void addBookProfileToList(List<MemberBookProfileResponse> list,
                                      MemberBookProfileResponse i) {
        if (i.isBookIsRepresentative()) {
            list.add(0, i);
        } else {
            list.add(i);
        }
    }

    private void validateDuplicatedNickname(String nickname) {
        memberRepository.findByMemberProfileName(nickname)
                .ifPresent(profile -> {
                    throw new BaseException(MemberProfileException.ALREADY_EXISTS_NICKNAME);
                });
    }
}
