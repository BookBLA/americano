package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminMemberProfileStatusResponse {

    private final List<String> memberProfileImageStatuses;
    private final List<String> memberOpenKakaoRoomStatuses;
    private final List<String> memberStudentIdStatuses;
    private final List<String> memberVerifyStatuses;

    public static AdminMemberProfileStatusResponse of(
            List<String> profileImageStatuses,
            List<String> openKakaoRoomStatuses,
            List<String> studentIdStatuses,
            List<String> memberVerifyStatuses
    ) {
        return new AdminMemberProfileStatusResponse(
                profileImageStatuses, openKakaoRoomStatuses,
                studentIdStatuses, memberVerifyStatuses
        );
    }
}
