package com.bookbla.americano.domain.matching.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshMemberRequest {

    @NotNull(message = "변경할 회원의 ID가 입력되지 않았습니다.")
    private Long refreshMemberId;

    @NotNull(message = "변경할 회원의 책 ID가 입력되지 않았습니다.")
    private Long refreshMemberBookId;
}