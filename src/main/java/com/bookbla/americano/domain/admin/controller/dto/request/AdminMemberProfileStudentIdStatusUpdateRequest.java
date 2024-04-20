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
public class AdminMemberProfileStudentIdStatusUpdateRequest {

    @NotBlank(message = "변경할 생태가 입력되지 않았습니다")
    private String updateStatus;

    public StatusUpdateDto toDto(Long memberId) {
        return new StatusUpdateDto(memberId, updateStatus);
    }

}
