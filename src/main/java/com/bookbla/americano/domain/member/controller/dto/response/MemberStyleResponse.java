package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberStyleResponse {

    private final Long memberId;
    private final String smokeType;
    private final String mbti;
    private final int height;
    private final int profileImageTypeId;
    private final String profileImageUrl;

    public static MemberStyleResponse of(Member member, MemberStyle memberStyle) {
        return new MemberStyleResponse(
                member.getId(),
                memberStyle.getSmokeType().getDetailValue(),
                memberStyle.getMbti().name(),
                memberStyle.getHeight(),
                memberStyle.getProfileImageType().getId(),
                memberStyle.getProfileImageType().getImageUrl()
        );
    }
}
