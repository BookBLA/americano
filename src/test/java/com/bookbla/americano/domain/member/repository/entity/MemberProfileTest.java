package com.bookbla.americano.domain.member.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

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

}
