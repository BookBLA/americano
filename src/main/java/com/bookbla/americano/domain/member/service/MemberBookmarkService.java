package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberBookmarkService {
    private final MemberBookmarkRepository memberBookmarkRepository;


    public int getMemberBookmarkCount(Long memberId) {
        MemberBookmark result = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        return result.getBookmarkCount();
    }

}