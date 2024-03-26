package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class AdminMemberProfileStatusUpdateRequest {

    @NotBlank(message = "변경할 상태가 입력되지 않았습니다")
    private String status;

    public StatusUpdateDto toDto(Long memberProfileId) {
        return new StatusUpdateDto(memberProfileId, status);
    }
}
