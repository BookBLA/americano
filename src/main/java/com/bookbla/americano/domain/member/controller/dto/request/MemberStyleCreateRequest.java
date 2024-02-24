package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberStyleCreateRequest {

    @NotNull(message = "mbti가 입력되지 않았습니다.")
    private String mbti;

    @NotNull(message = "음주 취향이 입력되지 않았습니다.")
    private String drinkType;

    @NotNull(message = "데이트 취향이 입력되지 않았습니다.")
    private String dateCostType;

    @NotNull(message = "흡연 타입이 입력되지 않았습니다.")
    private String smokeType;

    @NotNull(message = "연락 취향이 입력되지 않았습니다.")
    private String contactType;

    @NotNull(message = "남-여사친 취향이 입력되지 않았습니다.")
    private String justFriendType;

    @NotNull(message = "데이트 스타일이 입력되지 않았습니다.")
    private String dateStyle;

    public MemberStyle toMemberStyleWith(Member member) {
        return MemberStyle.builder()
                .member(member)
                .dateStyleType(DateStyleType.from(dateStyle))
                .contactType(ContactType.from(contactType))
                .smokeType(SmokeType.from(smokeType))
                .mbti(Mbti.from(mbti))
                .drinkType(DrinkType.from(drinkType))
                .justFriendType(JustFriendType.from(justFriendType))
                .dateCostType(DateCostType.from(dateCostType))
                .build();
    }
}
