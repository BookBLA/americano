package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.AdmobType;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAdmobRequest {

    @NotBlank(message = "애드몹 타입이 입력되지 않았습니다.")
    private String admobType;

    public AdmobType getAdmobType() {
        return AdmobType.from(admobType);
    }
}
