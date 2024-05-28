package com.bookbla.americano.domain.member.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileImageUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileOpenKakaoRoomUrlUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import com.bookbla.americano.domain.member.service.dto.MemberProfileStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MemberVerifyRepository memberVerifyRepository;

    @Override
    public MemberProfileResponse createMemberProfile(Long memberId, MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberEmail memberEmail = memberEmailRepository.findByMember(member)
                .orElseThrow(() -> new BaseException(MemberEmailExceptionType.EMAIL_NOT_REGISTERED));
        memberEmail.validatePending();

        saveProfileImageVerify(member, memberProfileDto.getProfileImageUrl());
        saveKakaoRoomVerify(member, memberProfileDto.getOpenKakaoRoomUrl());
        saveStudentIdVerify(member, memberProfileDto.toMemberVerifyDescription(), memberProfileDto.getStudentIdImageUrl());

        MemberProfile memberProfile = memberProfileDto.toMemberProfile();
        member.updateMemberProfile(memberProfile)
                .updateMemberStatus(MemberStatus.APPROVAL);

        return MemberProfileResponse.from(member, memberProfile);
    }

    private void saveStudentIdVerify(Member member, String verifyDescription, String studentImageUrl) {
        MemberVerify studentIdVerify = MemberVerify.builder()
                .memberId(member.getId())
                .description(verifyDescription)
                .contents(studentImageUrl)
                .verifyType(MemberVerifyType.STUDENT_ID)
                .build();
        memberVerifyRepository.save(studentIdVerify);
    }

    private void saveProfileImageVerify(Member member, String imageUrl) {
        MemberVerify profileImageVerify = MemberVerify.builder()
                .memberId(member.getId())
                .contents(imageUrl)
                .verifyType(MemberVerifyType.PROFILE_IMAGE)
                .build();
        memberVerifyRepository.save(profileImageVerify);
    }

    private void saveKakaoRoomVerify(Member member, String kakaoRoomUrl) {
        MemberVerify kakaoRoomVerify = MemberVerify.builder()
                .memberId(member.getId())
                .contents(kakaoRoomUrl)
                .verifyType(MemberVerifyType.OPEN_KAKAO_ROOM_URL)
                .build();
        memberVerifyRepository.save(kakaoRoomVerify);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileResponse readMemberProfile(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Override
    public MemberProfileResponse updateMemberProfile(Long memberId, MemberProfileUpdateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        saveProfileImageVerify(member, request.getProfileImageUrl());
        saveKakaoRoomVerify(member, request.getOpenKakaoRoomUrl());
        saveStudentIdVerify(member, request.toVerifyDescription(), request.getStudentIdImageUrl());

        return MemberProfileResponse.from(member, memberProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberProfileStatusResponse readMemberProfileStatus(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();
        return MemberProfileStatusResponse.from(memberProfile);
    }

    @Override
    public MemberProfileStatusResponse updateMemberProfileStatus(
            Long memberId,
            MemberProfileStatusDto dto
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateProfileImageStatus(dto.getProfileImageStatus())
                .updateOpenKakaoRoomStatus(dto.getOpenKakaoRoomStatus())
                .updateStudentIdImageStatus(dto.getStudentIdImageStatus());

        return MemberProfileStatusResponse.from(memberProfile);
    }

    @Override
    public void updateMemberProfileImage(
            Long memberId, MemberProfileImageUpdateRequest request
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        saveProfileImageVerify(member, request.getProfileImageUrl());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateProfileImageStatus(ProfileImageStatus.PENDING);
    }

    @Override
    public void updateMemberProfileKakaoRoom(
            Long memberId, MemberProfileOpenKakaoRoomUrlUpdateRequest request
    ) {
        Member member = memberRepository.getByIdOrThrow(memberId);

        saveKakaoRoomVerify(member, request.getOpenKakaoRoomUrl());

        MemberProfile memberProfile = member.getMemberProfile();
        memberProfile.updateOpenKakaoRoomStatus(OpenKakaoRoomStatus.PENDING);
    }

    @Override
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


    @Override
    @Transactional(readOnly = true)
    public List<MemberBookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto) {
        List<MemberBookProfileResponse> allResult = memberRepository.getAllMembers(
                memberId, requestDto);
        // 탐색 결과가 없을 때, EMPTY_MEMBER_BOOK Exception(해당 사용자의 선호 책도 없음)
        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }
        return allResult;
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
}
