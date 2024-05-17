package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.MemberStatusUpdateDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberStatusUpdateRequest {

    private String openKakaoRoomStatus;
    private String profileImageStatus;
    private String studentIdImageStatus;

    public MemberStatusUpdateDto toDto(Long memberId) {
        return new MemberStatusUpdateDto(memberId, openKakaoRoomStatus, profileImageStatus, studentIdImageStatus);
    }
}
