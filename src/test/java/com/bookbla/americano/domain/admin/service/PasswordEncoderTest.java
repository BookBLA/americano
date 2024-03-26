package com.bookbla.americano.domain.admin.service;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BcryptPasswordEncoder();

    @Test
    void 평문을_암호화_할_수_있다() {
        // given
        String before = "북블라최고";

        // when
        String after = passwordEncoder.encode(before);

        // then
        assertThat(passwordEncoder.matches(before, after)).isTrue();
    }

}
