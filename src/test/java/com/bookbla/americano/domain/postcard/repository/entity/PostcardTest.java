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

    @EnumSource(mode = INCLUDE, names = {"PENDING", "ACCEPT", "ALL_WRONG", "READ"})
    @ParameterizedTest(name = "기존_전송한_엽서의_상태에_따라_새로_엽서를_전송할_수_없다")
    void 기존_전송한_엽서의_상태에_따라_새로_엽서를_전송할_수_없다(PostcardStatus status) {
        // given
        Postcard postcard = Postcard.builder().postcardStatus(status).build();

        // when, then
        assertThatThrownBy(postcard::validateSendPostcard)
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(" 엽서가 존재합니다.");
    }

    @Test
    void 기존_전송한_엽서가_거절되었다면_새로_엽서를_보낼_수_있다() {
        // given
        Postcard postcard = Postcard.builder().postcardStatus(PostcardStatus.REFUSED).build();

        // when, then
        assertDoesNotThrow(postcard::validateSendPostcard);
    }
}
