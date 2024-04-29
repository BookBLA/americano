package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberKakaoRoomStatusUpdateRequest {

    @NotBlank(message = "변경할 상태가 입력되지 않았습니다")
    private String status;

    @NotNull(message = "사유가 입력되지 않았습니다")
    private String reason;

    public StatusUpdateDto toDto(Long memberVerifyId) {
        return new StatusUpdateDto(memberVerifyId, status, reason);
    }
}
