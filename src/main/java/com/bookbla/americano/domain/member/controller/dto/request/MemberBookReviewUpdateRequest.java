package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberBookReviewUpdateRequest {

    @NotBlank(message = "감상문이 입력되지 않았습니다")
    private String contents;

}
