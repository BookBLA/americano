package com.bookbla.americano.domain.admin.controller.dto.request;

import com.bookbla.americano.domain.admin.service.dto.MemberStatusUpdateDto;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminMemberStatusUpdateRequest {

    @NotBlank(message = "카카오톡 오픈 채팅방 상태가 입력되지 않았습니다")
    private String openKakaoRoomStatus;

    @NotBlank(message = "프로필 이미지 상태가 입력되지 않았습니다")
    private String profileImageStatus;

    @NotBlank(message = "학생증 인증 상태가 입력되지 않았습니다")
    private String studentIdImageStatus;

    public MemberStatusUpdateDto toDto(Long memberId) {
        return new MemberStatusUpdateDto(memberId, openKakaoRoomStatus, profileImageStatus, studentIdImageStatus);
    }
}
