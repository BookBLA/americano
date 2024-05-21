package com.bookbla.americano.domain.postcard.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PostcardTest {

    @EnumSource(mode = INCLUDE, names = {"PENDING", "ACCEPT", "ALL_WRONG"})
    @ParameterizedTest(name = "엽서를_보낼_수_없다면_예외를_던진다")
    void 엽서를_보낼_수_없다면_예외를_던진다(PostcardStatus status) {
        // given
        Postcard postcard = Postcard.builder().postcardStatus(status).build();

        // when, then
        assertThatThrownBy(postcard::validateSendPostcard)
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(" 엽서가 존재합니다.");
    }

    @Test
    void 엽서를_보낼_수_있는지_검증한다() {
        // given
        Postcard postcard = Postcard.builder().postcardStatus(PostcardStatus.REFUSED).build();

        // when, then
        assertDoesNotThrow(postcard::validateSendPostcard);
    }
}
