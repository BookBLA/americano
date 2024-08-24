package com.bookbla.americano.fixture;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;

import static com.bookbla.americano.domain.member.enums.Gender.MALE;

public class Fixture {

    public static final Member MALE_MEMBER = Member.builder()
            .memberProfile(MemberProfile.builder()
                    .gender(MALE)
                    .build())
            .build();
}
