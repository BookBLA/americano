package com.bookbla.americano.domain.matching.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class RejectMemberRequest {

    @NotNull(message = "거절할 회원의 ID가 입력되지 않았습니다.")
    private Long rejectedMemberId;
}
