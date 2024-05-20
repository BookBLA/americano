package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import com.bookbla.americano.domain.member.service.MemberPostcardService;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPostcardServiceImpl implements MemberPostcardService {
    private final MemberPostcardRepository memberPostcardRepository;

    @Override
    public MemberPostcardResponse getMemberPostcardEachCount(Long memberId) {
        MemberPostcard result = memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_POSTCARD_INFO));
        return new MemberPostcardResponse(result.getFreePostcardCount(), result.getPayPostcardCount());
    }

    @Override
    public int getMemberPostcardCount(Long memberId) {
        MemberPostcard result = memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_POSTCARD_INFO));

        return getPostcardTotal(result);
    }

    private int getPostcardTotal(MemberPostcard memberPostcard) {
        return memberPostcard.getFreePostcardCount() + memberPostcard.getPayPostcardCount();
    }
}
