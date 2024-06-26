package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.NotificationDto;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberNotificationRequest {

    @NotNull(message = "알림을 보낼 회원이 입력되지 않았습니다.")
    private Long memberId;

    @NotBlank(message = "알림 제목이 입력되지 않았습니다")
    private String title;

    @NotBlank(message = "알림 내용이 입력되지 않았습니다")
    private String contents;

    public NotificationDto toDto() {
        return new NotificationDto(title, contents);
    }
}
