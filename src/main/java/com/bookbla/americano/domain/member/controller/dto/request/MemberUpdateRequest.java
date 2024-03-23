package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberUpdateRequest {

    @NotNull(message = "이메일이 입력되지 않았습니다.")
    private String oauthEmail;

    @NotNull(message = "멤버 타입이 입력되지 않았습니다.")
    private String memberType;

    @NotNull(message = "멤버 상태가 입력되지 않았습니다.")
    private String memberStatus;

    public MemberType getMemberType() {
        return MemberType.from(memberType);
    }

    public MemberStatus getMemberStatus() {
        return MemberStatus.from(memberStatus);
    }

}
