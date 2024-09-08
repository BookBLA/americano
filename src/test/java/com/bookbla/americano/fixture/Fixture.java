package com.bookbla.americano.fixture;

import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;

import static com.bookbla.americano.domain.member.enums.Gender.MALE;

public class Fixture {

    public static final Member 스타일_등록_완료_남성_고도리 = Member.builder()
            .memberProfile(MemberProfile.builder()
                    .name("고도리")
                    .gender(MALE)
                    .build())
            .memberStyle(MemberStyle.builder()
                    .height(175)
                    .mbti(Mbti.ESFP)
                    .smokeType(SmokeType.NON_SMOKE)
                    .build())
            .build();

    public static final Member 프로필_등록_완료_남성_리준희 = Member.builder()
            .memberProfile(MemberProfile.builder()
                    .name("리준희")
                    .gender(MALE)
                    .build())
            .build();
}
