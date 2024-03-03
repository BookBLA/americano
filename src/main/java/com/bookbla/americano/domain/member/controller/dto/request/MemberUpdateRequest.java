package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.MemberType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberUpdateRequest {

    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String oauthEmail;

    @NotNull(message = "멤버 타입이 입력되지 않았습니다.")
    private String memberType;

    public MemberType getMemberType() {
        return MemberType.from(memberType);
    }

}