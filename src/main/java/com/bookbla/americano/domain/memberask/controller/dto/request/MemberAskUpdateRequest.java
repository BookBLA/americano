package com.bookbla.americano.domain.memberask.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAskUpdateRequest {

    // 등록하지 않을 수도 있나?
    @NotNull(message = "개인 질문이 입력되지 않았습니다.")
    private String contents;

}
