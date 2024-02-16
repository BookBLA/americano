package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberProfileRepository repository;
	@Override
    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }

    @Override
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        update(member, memberUpdateRequest);

        return MemberResponse.from(member);
    }

    private void update(Member member, MemberUpdateRequest request) {
        member.updateOauthEmail(request.getOauthEmail())
            .updateMemberType(request.getMemberType());
    }

	@Override
    public Member getMemberById(Long memberId) {
        return memberRepository.getByIdOrThrow(memberId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MemberBookProfileResponseDto> findSameBookMembers(MemberBookProfileRequestDto requestDto) {
        List<MemberBookProfileResponseDto> allResult = repository.searchSameBookMember(requestDto);
        // 탐색 결과가 없을 때, EMPTY_MEMBER_BOOK Exception(해당 사용자의 선호 책도 없음)
        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }
        // 해당 사용자의 선호 책
        List<MemberBookProfileResponseDto> userBookProfile = new ArrayList<>();
        List<MemberBookProfileResponseDto> othersBookProfile = new ArrayList<>(allResult);
        for (MemberBookProfileResponseDto i : allResult) {
            if (i.getMemberId() == requestDto.getMemberId()) {
                addBookProfileToList(userBookProfile, i);
                othersBookProfile.remove(i);
            }
        }
        // 해당 사용자의 선호책이 없을 때, EMPTY_MEMBER_BOOK Exception
        if (userBookProfile.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }

        List<MemberBookProfileResponseDto> result = new ArrayList<>();
        Map<Long, MemberBookProfileResponseDto> selected = new HashMap<>();
        int afterRepresentativeIdx = -1;
        for (MemberBookProfileResponseDto i : userBookProfile) {
            List<MemberBookProfileResponseDto> bookProfiles = othersBookProfile.stream()
                    .filter(bp -> i.getBookId() == bp.getBookId())
                    .collect(Collectors.toList());
            for (MemberBookProfileResponseDto j : bookProfiles) {
                addBookProfileToList(result, i, j, selected, afterRepresentativeIdx, requestDto.getGender());
            }
            if (afterRepresentativeIdx == -1)
                afterRepresentativeIdx = result.size();
        }
        return result;
    }
    
    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.getByIdOrThrow(memberId);
    }

    private void addBookProfileToList(List<MemberBookProfileResponseDto> list, MemberBookProfileResponseDto i) {
        if (i.isBookIsRepresentative())
            list.add(0, i);
        else
            list.add(i);
    }

    private void addBookProfileToList(List<MemberBookProfileResponseDto> result, MemberBookProfileResponseDto i, MemberBookProfileResponseDto j, Gender gender) {
        if (gender == null) {
            result.add(j);
        } else if (gender == j.getMemberGender()) {
            if (j.isBookIsRepresentative() && i.isBookIsRepresentative())
                result.add(0, j);
            else
                result.add(j);
        }
    }

    private void addBookProfileToList(List<MemberBookProfileResponseDto> result, MemberBookProfileResponseDto i, MemberBookProfileResponseDto j,
                                      Map<Long, MemberBookProfileResponseDto> selected, int afterRepresentativeIdx, Gender gender) {
        if (selected.containsKey(j.getMemberId()) && j.isBookIsRepresentative() && result.indexOf(selected.get(j.getMemberId())) >= afterRepresentativeIdx) {
            result.add(afterRepresentativeIdx, j);
            result.remove(selected.get(j.getMemberId()));
            selected.remove(j.getMemberId());
            selected.put(j.getMemberId(), j);
        } else if (!selected.containsKey(j.getMemberId())) {
            addBookProfileToList(result, i, j, gender);
            selected.put(j.getMemberId(), j);
        }
    }
}
