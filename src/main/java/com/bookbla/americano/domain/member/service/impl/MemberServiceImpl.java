package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
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
    @Transactional(readOnly = true)
    public List<MemberBookProfileResponseDto> findSameBookMembers(MemberBookProfileRequestDto requestDto) {
        List<MemberBookProfileResponseDto> allResult = repository.searchSameBookMember(requestDto);

        if (allResult.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }
        List<MemberBookProfileResponseDto> userBookProfile = new ArrayList<>();
        for (MemberBookProfileResponseDto i : allResult) {
            if (i.getMemberId() == requestDto.getMemberId()) {
                if (i.isBookIsRepresentative())
                    userBookProfile.add(0, i);
                else
                    userBookProfile.add(i);
            }
        }

        if (userBookProfile.isEmpty()) {
            throw new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOK);
        }

        Map<Long, MemberBookProfileResponseDto> selected = new HashMap<>();
        List<MemberBookProfileResponseDto> result = new ArrayList<>();
        int afterRepresentativeIdx = -1;
        for (MemberBookProfileResponseDto i : userBookProfile) {
            for (MemberBookProfileResponseDto j : allResult) {
                if (i.getMemberId() != j.getMemberId() && i.getBookId() == j.getBookId()) {
                    if (selected.containsKey(j.getMemberId()) && result.indexOf(selected.get(j.getMemberId())) >= afterRepresentativeIdx && j.isBookIsRepresentative()) {
                        result.add(afterRepresentativeIdx, j);
                        result.remove(selected.get(j.getMemberId()));
                        selected.remove(j.getMemberId());
                        selected.put(j.getMemberId(), j);
                    } else if (!selected.containsKey(j.getMemberId())) {
                        if (requestDto.getGender() != null) {
                            if (requestDto.getGender() == j.getMemberGender()) {
                                if (j.isBookIsRepresentative() && i.isBookIsRepresentative())
                                    result.add(0, j);
                                else
                                    result.add(j);
                                selected.put(j.getMemberId(), j);
                            }
                        } else {
                            result.add(j);
                            selected.put(j.getMemberId(), j);
                        }
                    }
                }
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
}
