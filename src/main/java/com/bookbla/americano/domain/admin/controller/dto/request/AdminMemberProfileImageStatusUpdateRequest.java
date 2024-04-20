package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberProfileImageStatusUpdateRequest {

    @NotBlank(message = "변경할 상태가 입력되지 않았습니다")
    private String status;

    public StatusUpdateDto toDto(Long memberId) {
        return new StatusUpdateDto(memberId, status);
    }
}
