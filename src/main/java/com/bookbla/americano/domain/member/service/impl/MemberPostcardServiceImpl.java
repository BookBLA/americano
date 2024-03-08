package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberPostcardCountRequestDto;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import com.bookbla.americano.domain.member.service.MemberPostcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPostcardServiceImpl implements MemberPostcardService {
    private final MemberPostcardRepository memberPostcardRepository;

    @Override
    public int getMemberPostcardCount(MemberPostcardCountRequestDto requestDto) {
        MemberPostcard result = memberPostcardRepository.findMemberPostcardByMemberId(requestDto.getMemberId())
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_POSTCARD_INFO));

        return getPostcardTotal(result);
    }

    private int getPostcardTotal(MemberPostcard memberPostcard) {
        return memberPostcard.getFreePostcardCount() + memberPostcard.getPayPostcardCount();
    }
}
