package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberBookmarkAdmobResponse {

    private final int admobCount;

    public static MemberBookmarkAdmobResponse from(MemberBookmark memberBookmark) {
        return new MemberBookmarkAdmobResponse(memberBookmark.getAdmobCount());
    }
}
