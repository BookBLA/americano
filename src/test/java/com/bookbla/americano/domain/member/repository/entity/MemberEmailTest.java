package com.bookbla.americano.domain.member.repository.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberEmailTest {

    @Test
    void 이메일_도메인을_가져올_수_있다() {
        // given
        MemberEmail memberEmail = MemberEmail.builder()
                .schoolEmail("ligilya@gachon.ac.kr")
                .build();

        // when
        String actual = memberEmail.getEmailDomain();

        // then
        assertThat(actual).isEqualTo("@gachon.ac.kr");
    }
}
