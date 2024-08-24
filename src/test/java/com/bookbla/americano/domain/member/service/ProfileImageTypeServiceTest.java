package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.ProfileImageTypeRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.ProfileImageType;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.member.enums.Gender.FEMALE;
import static com.bookbla.americano.domain.member.enums.Gender.MALE;
import static com.bookbla.americano.fixture.Fixture.MALE_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class ProfileImageTypeServiceTest {

    private static final ProfileImageType MALE_DEFAULT = ProfileImageType.builder()
            .gender(MALE)
            .imageUrl("남자사진")
            .build();

    private static final ProfileImageType FEMALE_DEFAULT = ProfileImageType.builder()
            .gender(FEMALE)
            .imageUrl("여자사진")
            .build();

    @Autowired
    private ProfileImageTypeService sut;

    @Autowired
    private ProfileImageTypeRepository profileImageTypeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 동일한_성별의_프로필_사진을_찾을_수_있다() {
        // given
        profileImageTypeRepository.save(FEMALE_DEFAULT);
        profileImageTypeRepository.save(MALE_DEFAULT);
        Member male = memberRepository.save(MALE_MEMBER);

        // when
        var response = sut.readMemberGenderProfileImageTypes(male.getId());

        // then
        assertThat(response.getProfileImageResponseTypes())
                        .hasSize(1)
                        .extracting("gender", "profileImageUrl")
                        .containsExactly(tuple("MALE", "남자사진"));

    }
}
