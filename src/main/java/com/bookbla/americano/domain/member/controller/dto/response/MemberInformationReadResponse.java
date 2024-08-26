package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberInformationReadResponse {

    private final String name;
    private final String mbti;
    private final String smokeType;
    private final int height;

    public static MemberInformationReadResponse from(Member member) {
        MemberStyle memberStyle = member.getMemberStyle();
        return new MemberInformationReadResponse(
                member.getMemberProfile().getName(),
                memberStyle.getMbti().name(),
                memberStyle.getSmokeType().getValue(),
                memberStyle.getHeight()
        );
    }
}
