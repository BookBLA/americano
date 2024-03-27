package com.bookbla.americano.domain.admin.repository.entity;

import java.time.LocalDateTime;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AdminSessionTest {

    @Test
    void 만료_기한이_지난_경우_예외가_발생한다() {
        // given
        AdminSession adminSession = new AdminSession(1L, "UUID", LocalDateTime.now().minusMinutes(1));

        // when, then
        assertThatThrownBy(adminSession::validateExpired)
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("세션이 만료되었습니다");
    }
}
