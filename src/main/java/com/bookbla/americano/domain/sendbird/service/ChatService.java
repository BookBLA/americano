package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final MemberBookmarkRepository memberBookmarkRepository;

    public void chatAccept(Long targetMemberId) {
        MemberBookmark targetMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(targetMemberId) // 채팅 수락한 사람 책갈피
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        targetMemberBookmark.acceptChat();
    }

    public void chatReject(Long targetMemberId) {
        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(targetMemberId) // 채팅 보낸 사람 책갈피
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        memberBookmark.rejectChat();
    }
}