package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import io.micrometer.core.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberBookProfileRequestDto {

    @Nullable
    private Gender gender;

    @Nullable
    private DrinkType drinkType;

    @Nullable
    private SmokeType smokeType;

    @Nullable
    private ContactType contactType;

    @Nullable
    private DateStyleType dateStyleType;

    @Nullable
    private DateCostType dateCostType;

    @Nullable
    private Mbti mbti;

    @Nullable
    private JustFriendType justFriendType;
}
