package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class MemberBookUpdateRequest {

    @NotNull(message = "감상문이 입력되지 않았습니다")
    private String contents;

}
