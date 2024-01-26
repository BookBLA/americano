package com.bookbla.americano.domain.member;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.persistence.Embedded;
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
public class MemberFavorite extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Embedded
    @Enumerated(EnumType.STRING)
    private SmokeType smokeType;

    @Embedded
    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;

    @Embedded
    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @Embedded
    @Enumerated(EnumType.STRING)
    private DateCostType dateCostType;

    @Embedded
    @Enumerated(EnumType.STRING)
    private JustFriendType justFriendType;

    @Embedded
    @Enumerated(EnumType.STRING)
    private Mbti mbti;

}
