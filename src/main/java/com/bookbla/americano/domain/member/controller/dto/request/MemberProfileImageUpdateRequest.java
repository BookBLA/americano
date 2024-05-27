package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberProfileImageUpdateRequest {

    @NotBlank(message = "프로필 이미지 링크가 입력되지 않았습니다")
    private String profileImageUrl;

}
