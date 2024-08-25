package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberInformationUpdateRequest {

    @NotNull(message = "이름이 입력되지 않았습니다")
    private String name;

    @NotNull(message = "MBTI가 입력되지 않았습니다.")
    private String mbti;

    @NotNull(message = "흡연 유형이 입력되지 않았습니다")
    private String smokeType;

    @NotNull(message = "키가 입력되지 않았습니다")
    private Integer height;

}
