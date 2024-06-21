package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberBlockCreateRequest {

    @NotNull(message = "차단될 멤버의 아이디가 입력되지 않았습니다.")
    private Long blockedByMemberId;
}
