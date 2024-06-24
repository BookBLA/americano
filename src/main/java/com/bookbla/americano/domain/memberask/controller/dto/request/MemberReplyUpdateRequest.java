package com.bookbla.americano.domain.memberask.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberReplyUpdateRequest {
    @NotNull(message = "엽서 id가 입력되지 않았습니다.")
    private Long postcardId;
    @NotBlank(message = "답변이 입력되지 않았습니다.")
    private String content;
}
