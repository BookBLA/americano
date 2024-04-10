package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberStyleResponse {

    private final Long memberId;
    private final String smokeType;
    private final String contactType;
    private final String dateCostType;
    private final String dateStyleType;
    private final String justFriendType;
    private final String drinkType;
    private final String mbti;
    private final String memberAsk;

    public static MemberStyleResponse of(Member member, MemberStyle memberStyle, MemberAsk memberAsk) {
        return new MemberStyleResponse(
                member.getId(),
                memberStyle.getSmokeType().getValue(),
                memberStyle.getContactType().getValue(),
                memberStyle.getDateCostType().getValue(),
                memberStyle.getDateStyleType().getValue(),
                memberStyle.getJustFriendType().getValue(),
                memberStyle.getDrinkType().getValue(),
                memberStyle.getMbti().name(),
                memberAsk.getContents()
        );
    }
}
