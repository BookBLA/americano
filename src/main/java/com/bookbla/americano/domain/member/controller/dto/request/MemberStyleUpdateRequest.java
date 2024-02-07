package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyle;
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
public class MemberStyleUpdateRequest {

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

    public Mbti getMbti() {
        return Mbti.from(mbti);
    }

    public DrinkType getDrinkType() {
        return DrinkType.from(drinkType);
    }

    public DateCostType getDateCostType() {
        return DateCostType.from(dateCostType);
    }

    public SmokeType getSmokeType() {
        return SmokeType.from(smokeType);
    }

    public ContactType getContactType() {
        return ContactType.from(contactType);
    }

    public JustFriendType getJustFriendType() {
        return JustFriendType.from(justFriendType);
    }

    public DateStyle getDateStyle() {
        return DateStyle.from(dateStyle);
    }
}
