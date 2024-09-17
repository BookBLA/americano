package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MemberBookmarkAdmobResponse {

    private int bookmarkCount;
    private int freeBookmarkAdmobCount;
    private int newPersonAdmobCount;

    public static MemberBookmarkAdmobResponse from(MemberBookmark memberBookmark) {
        return MemberBookmarkAdmobResponse.builder()
                .bookmarkCount(memberBookmark.getBookmarkCount())
                .freeBookmarkAdmobCount(memberBookmark.getFreeBookmarkAdmobCount())
                .newPersonAdmobCount(memberBookmark.getNewPersonAdmobCount())
                .build();
    }
}
