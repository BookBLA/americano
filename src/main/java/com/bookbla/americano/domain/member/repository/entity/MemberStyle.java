package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyle;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberStyle extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private SmokeType smokeType;

    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Enumerated(EnumType.STRING)
    private DateStyle dateStyle;

    @Enumerated(EnumType.STRING)
    private DateCostType dateCostType;

    @Enumerated(EnumType.STRING)
    private JustFriendType justFriendType;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    public void updateSmokeType(SmokeType smokeType) {
        this.smokeType = smokeType;
    }

    public void updateDrinkType(DrinkType drinkType) {
        this.drinkType = drinkType;
    }

    public void updateContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public void updateDateCostType(DateCostType dateCostType) {
        this.dateCostType = dateCostType;
    }

    public void updateJustFriendType(JustFriendType justFriendType) {
        this.justFriendType = justFriendType;
    }

    public void updateMbti(Mbti mbti) {
        this.mbti = mbti;
    }

    public void updateDateStyle(DateStyle dateStyle) {
        this.dateStyle = dateStyle;
    }
}
