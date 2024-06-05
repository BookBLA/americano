package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.NotificationDto;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmCreateRequest;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberNotificationRequest {

    @NotBlank(message = "알림 제목이 입력되지 않았습니다")
    private String title;

    @NotBlank(message = "알림 내용이 입력되지 않았습니다")
    private String contents;

    public PushAlarmCreateRequest toDto(Long memberId) {
        return new PushAlarmCreateRequest(memberId, title, contents);
    }

    public NotificationDto toDto() {
        return new NotificationDto(title, contents);
    }
}
