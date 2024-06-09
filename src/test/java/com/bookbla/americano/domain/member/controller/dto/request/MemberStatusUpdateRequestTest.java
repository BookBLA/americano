package com.bookbla.americano.domain.member.controller.dto.request;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberStatusUpdateRequestTest {

    @Test
    void 역직렬화_시_멤버_상태를_열거형으로_반환한다() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        MemberStatusUpdateRequest memberStatusUpdateRequest = objectMapper.readValue(
            "{\"memberStatus\":\"MATCHING_DISABLED\"}", MemberStatusUpdateRequest.class);

        Assertions.assertThat(memberStatusUpdateRequest.getMemberStatus()).isEqualTo(
            MemberStatus.MATCHING_DISABLED);
    }

    @Test
    void 존재하지_않는_멤버_상태를_받으면_예외를_던진다() {
        ObjectMapper objectMapper = new ObjectMapper();

        Assertions.assertThatThrownBy(() -> {
                objectMapper.readValue(
                    "{\"memberStatus\":\"FCUK\", \"reason\":\"Test reason\"}",
                    MemberStatusUpdateRequest.class);
            }).isInstanceOf(ValueInstantiationException.class)
            .hasCauseInstanceOf(BaseException.class);
    }

}