package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.HeightType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStyle {

    @Enumerated(EnumType.STRING)
    private SmokeType smokeType;

    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    private DateStyleType dateStyleType;

    @Enumerated(EnumType.STRING)
    private DateCostType dateCostType;

    @Enumerated(EnumType.STRING)
    private JustFriendType justFriendType;

    @Enumerated(EnumType.STRING)
    private HeightType heightType;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    public MemberStyle updateSmokeType(SmokeType smokeType) {
        this.smokeType = smokeType;
        return this;
    }

    public MemberStyle updateDrinkType(DrinkType drinkType) {
        this.drinkType = drinkType;
        return this;
    }

    public MemberStyle updateContactType(ContactType contactType) {
        this.contactType = contactType;
        return this;
    }

    public MemberStyle updateDateCostType(DateCostType dateCostType) {
        this.dateCostType = dateCostType;
        return this;
    }

    public MemberStyle updateJustFriendType(JustFriendType justFriendType) {
        this.justFriendType = justFriendType;
        return this;
    }

    public MemberStyle updateMbti(Mbti mbti) {
        this.mbti = mbti;
        return this;
    }

    public MemberStyle updateDateStyleType(DateStyleType dateStyleType) {
        this.dateStyleType = dateStyleType;
        return this;
    }
}
