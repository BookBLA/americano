package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.service.MemberProfileService;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    @Override
    public MemberProfileResponse createMemberProfile(Long memberId, MemberProfileDto memberProfileDto) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberProfile memberProfile = memberProfileRepository.save(memberProfileDto.toEntity(member));

        return MemberProfileResponse.from(member, memberProfile);
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

    public void update(MemberProfile memberProfile, MemberProfileUpdateRequest request) {
        memberProfile.updateName(request.getName())
            .updateBirthDate(request.getBirthDate())
            .updateSchoolName(request.getSchoolName())
            .updateGender(request.getGender())
            .updateOpenKakaoRoomUrl(request.getOpenKakaoRoomUrl());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberBookProfileResponseDto> findSameBookMembers(MemberBookProfileRequestDto requestDto) {
        List<MemberBookProfileResponseDto> allResult = memberProfileRepository.searchSameBookMember(requestDto);
        // 탐색 결과가 없을 때, EMPTY_MEMBER_BOOK Exception(해당 사용자의 선호 책도 없음)
        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }
        // 해당 사용자의 선호 책
        List<MemberBookProfileResponseDto> userBookProfiles = new ArrayList<>();
        List<MemberBookProfileResponseDto> otherBookProfiles = new ArrayList<>(allResult);
        for (MemberBookProfileResponseDto i : allResult) {
            if (i.getMemberId() == requestDto.getMemberId()) {
                addBookProfileToList(userBookProfiles, i);
                otherBookProfiles.remove(i);
            }
        }
        // 해당 사용자의 선호책이 없을 때, EMPTY_MEMBER_BOOK Exception
        if (userBookProfiles.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }

        // 1순위 : 대표책 - 대표책
        List<MemberBookProfileResponseDto> firstResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                MemberBookProfileResponseDto::isBookIsRepresentative,
                MemberBookProfileResponseDto::isBookIsRepresentative
        );

        Collections.shuffle(firstResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, firstResults);

        // 2순위 : 대표책 - 선호책
        List<MemberBookProfileResponseDto> secondResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                MemberBookProfileResponseDto::isBookIsRepresentative,
                otherBookProfile -> !otherBookProfile.isBookIsRepresentative()
        );

        Collections.shuffle(secondResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, secondResults);

        // 3순위 : 선호책 - 대표책
        List<MemberBookProfileResponseDto> thirdResults = findMatches(
                userBookProfiles,
                otherBookProfiles,
                userBookProfile -> !userBookProfile.isBookIsRepresentative(),
                MemberBookProfileResponseDto::isBookIsRepresentative
        );

        Collections.shuffle(thirdResults);
        removeMemberToOtherBookProfiles(otherBookProfiles, thirdResults);

        // 4순위 : 선호책 - 선호책
        List<MemberBookProfileResponseDto> fourthResults = findMatches(
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

    private List<MemberBookProfileResponseDto> findMatches(
            List<MemberBookProfileResponseDto> userBookProfiles,
            List<MemberBookProfileResponseDto> otherBookProfiles,
            Predicate<MemberBookProfileResponseDto> userPredicate,
            Predicate<MemberBookProfileResponseDto> otherPredicate) {

        return new ArrayList<>(userBookProfiles.stream()
                .filter(userPredicate)
                .flatMap(userBookProfile -> otherBookProfiles.stream()
                        .filter(otherBookProfile -> otherBookProfile.getBookId()
                                == userBookProfile.getBookId())
                        .filter(otherPredicate)
                ).collect(Collectors.groupingBy(MemberBookProfileResponseDto::getMemberId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                memberBookProfileList -> memberBookProfileList.get(0)
                        )))
                .values());
    }

    private void removeMemberToOtherBookProfiles(
            List<MemberBookProfileResponseDto> otherBookProfiles,
            List<MemberBookProfileResponseDto> resultBookProfiles) {
        otherBookProfiles.removeIf(otherBookProfile -> resultBookProfiles.stream()
                .anyMatch(resultBookProfile -> resultBookProfile.getMemberId()
                        == otherBookProfile.getMemberId()));
    }

    private void addBookProfileToList(List<MemberBookProfileResponseDto> list, MemberBookProfileResponseDto i) {
        if (i.isBookIsRepresentative())
            list.add(0, i);
        else
            list.add(i);
    }
}
