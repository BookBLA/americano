package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.MemberStatus;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberStatusUpdateRequest {

    @NotBlank(message = "변경할 상태를 입력하지 않았습니다.")
    private MemberStatus memberStatus;
    
    private String reason;
}
