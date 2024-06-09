package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberProfileOpenKakaoRoomUrlUpdateRequest {

    @NotBlank(message = "오픈 카카오톡 방 링크가 입력되지 않았습니다.")
    private String openKakaoRoomUrl;

}
