package com.bookbla.americano.domain.payment.enums;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.exception.PaymentExceptionType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PaymentTypeTest {

    @Test
    void 결제_유형을_찾을_수_있다() {
        // given
        String expected = "google";

        // when
        PaymentType actual = PaymentType.from(expected);

        // then
        assertThat(actual).isEqualTo(PaymentType.GOOGLE);
    }

    @Test
    void 존재하지_않는_결제_유형일_경우_예외가_발생한다() {
        // given
        String expected = "토스페이먼츠";

        // when, then
        Assertions.assertThatThrownBy(() -> PaymentType.from(expected))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(PaymentExceptionType.PAYMENT_TYPE_NOT_FOUND.getMessage());
    }
}
