package com.bookbla.americano.domain.member.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberProfileTest {

    @Test
    void 생일이_지난_만_나이를_계산할_수_있다() {
        // given
        LocalDate baseDate = LocalDate.of(2024, 3, 1);
        LocalDate birthDate = LocalDate.of(2005, 3, 1);
        MemberProfile memberProfile = MemberProfile.builder()
                .birthDate(birthDate)
                .build();

        // when
        int age = memberProfile.calculateAge(baseDate);

        // then
        assertThat(age).isEqualTo(19);
    }

    @Test
    void 생일이_지나지_않은_만_나이를_계산할_수_있다() {
        // given
        LocalDate baseDate = LocalDate.of(2024, 3, 1);
        LocalDate birthDate = LocalDate.of(2005, 3, 2);
        MemberProfile memberProfile = MemberProfile.builder()
                .birthDate(birthDate)
                .build();

        // when
        int age = memberProfile.calculateAge(baseDate);

        // then
        assertThat(age).isEqualTo(18);
    }

    @ValueSource(strings = {"김", "김수", "김수한", "김수한무", "김수한무거", "김수한무거북", "김수한무거북이"})
    @ParameterizedTest(name = "이름은 첫 글자와 익명 두 글자를 합쳐_보여준다")
    void 이름은_첫_글자와_익명_두_글자를_합쳐_보여준다(String value) {
        // given
        MemberProfile memberProfile = MemberProfile.builder()
                .name(value)
                .build();

        // when
        String actual = memberProfile.showBlindName();

        // then
        assertThat(actual).isEqualTo("김OO");
    }

}
