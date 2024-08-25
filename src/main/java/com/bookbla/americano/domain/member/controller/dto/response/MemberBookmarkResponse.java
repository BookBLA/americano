package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberBookmarkResponse {

    private final int admobCount;

    public static MemberBookmarkResponse from(MemberBookmark memberBookmark) {
        return new MemberBookmarkResponse(memberBookmark.getAdmobCount());
    }
}
