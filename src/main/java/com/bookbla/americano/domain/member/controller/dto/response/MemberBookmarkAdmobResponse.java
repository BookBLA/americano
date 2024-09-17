package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.AdmobType;
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

    private String admobType;
    private int bookmarkCount;
    private int freeBookmarkCount;

    public static MemberBookmarkAdmobResponse of(AdmobType admobType, MemberBookmark memberBookmark) {
        return MemberBookmarkAdmobResponse.builder()
                .admobType(admobType.name())
                .bookmarkCount(memberBookmark.getBookmarkCount())
                .freeBookmarkCount(memberBookmark.getFreeBookmarkAdmobCount())
                .build();
    }
}
