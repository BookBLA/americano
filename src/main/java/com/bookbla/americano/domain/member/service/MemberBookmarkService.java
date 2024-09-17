package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberAdmobRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookmarkAdmobResponse;
import com.bookbla.americano.domain.member.enums.AdmobType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberBookmarkService {

    private final MemberBookmarkRepository memberBookmarkRepository;

    public int getMemberBookmarkCount(Long memberId) {
        MemberBookmark result = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        return result.getBookmarkCount();
    }

    @Transactional
    public MemberBookmarkAdmobResponse updateBookmarkByAdmob(Long memberId, MemberAdmobRequest request) {
        MemberBookmark bookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        AdmobType admobType = request.getAdmobType();
        bookmark.watchAdmob(admobType);

        return MemberBookmarkAdmobResponse.of(admobType, bookmark);
    }

    public MemberBookmarkAdmobResponse getMemberAdmob(Long memberId, String type) {
        AdmobType admobType = AdmobType.from(type);
        MemberBookmark bookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        return MemberBookmarkAdmobResponse.of(admobType, bookmark);
    }
}