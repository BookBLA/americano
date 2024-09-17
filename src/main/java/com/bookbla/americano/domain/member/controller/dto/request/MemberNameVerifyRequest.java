package com.bookbla.americano.domain.member.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberNameVerifyRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
}
