package com.bookbla.americano.domain.memberask.repository.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAskTest {

    private static final int MAX_CONTENTS_LENGTH = 80;

    @Test
    void 개인_질문의_제한_글자수를_초과하면_예외가_발생한다() {
        // given
        Member member = Member.builder().build();
        String content = " ".repeat(MAX_CONTENTS_LENGTH + 1);

        // when, then
        assertThatThrownBy(() -> MemberAsk.builder().member(member).contents(content).build())
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("개인 질문은 공백 포함 80자 이하로만 작성가능합니다");
    }

    @Test
    void 질문_내역_업데이트시_제한_글자수를_초과하면_예외가_발생한다() {
        // given
        Member member = Member.builder().build();
        MemberAsk memberAsk = MemberAsk.builder().member(member).contents("주로 어디서 책을 읽는 편이에요?")
                .build();

        String invalidSizeContents = " ".repeat(MAX_CONTENTS_LENGTH + 1);

        // when, then
        assertThatThrownBy(() -> memberAsk.updateContent(invalidSizeContents))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("개인 질문은 공백 포함 80자 이하로만 작성가능합니다");
    }

    @Test
    void 질문_생성시_제한_글자수를_초과하지_않으면_정상_생성된다() {
        // given
        Member member = Member.builder().build();

        String validSizeContents = " ".repeat(MAX_CONTENTS_LENGTH);

        // when, then
        assertDoesNotThrow(
                () -> MemberAsk.builder().member(member).contents(validSizeContents).build());
    }


}
