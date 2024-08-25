package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberProfileImageTypeUpdateRequest {

    @NotNull(message = "기본 프로필 이미지 타입 id가 입력되지 않았습니다")
    private Long profileImageTypeId;

}
