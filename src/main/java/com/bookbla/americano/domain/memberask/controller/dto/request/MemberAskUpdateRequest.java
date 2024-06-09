package com.bookbla.americano.domain.memberask.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAskUpdateRequest {

    @NotBlank(message = "개인 질문이 입력되지 않았습니다.")
    private String contents;

}
