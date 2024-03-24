package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.service.dto.MemberProfileStatusDto;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberProfileStatusUpdateRequest {

    @NotNull(message = "프로필 사진 상태가 입력되지 않았습니다.")
    private String profileImageStatus;

    @NotNull(message = "오픈카톡방 상태가 입력되지 않았습니다.")
    private String openKakaoRoomStatus;

    @NotNull(message = "학생증 상태가 입력되지 않았습니다.")
    private String studentIdImageStatus;

    public ProfileImageStatus getProfileImageStatus() {
        return ProfileImageStatus.from(profileImageStatus);
    }

    public OpenKakaoRoomStatus getOpenKakaoRoomStatus() {
        return OpenKakaoRoomStatus.from(openKakaoRoomStatus);
    }

    public StudentIdImageStatus getStudentIdImageStatus() {
        return StudentIdImageStatus.from(studentIdImageStatus);
    }

    public MemberProfileStatusDto toDto() {
        return MemberProfileStatusDto.builder()
            .profileImageStatus(getProfileImageStatus())
            .openKakaoRoomStatus(getOpenKakaoRoomStatus())
            .studentIdImageStatus(getStudentIdImageStatus())
            .build();
    }

}
