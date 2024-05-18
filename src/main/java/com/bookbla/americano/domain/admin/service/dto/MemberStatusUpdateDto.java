package com.bookbla.americano.domain.admin.service.dto;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MemberStatusUpdateDto {

    private final Long memberId;
    private final String openKakaoRoomStatus;
    private final String profileImageStatus;
    private final String studentIdImageStatus;

    public Long getMemberId() {
        return memberId;
    }

    public OpenKakaoRoomStatus getOpenKakaoRoomStatus() {
        return OpenKakaoRoomStatus.from(openKakaoRoomStatus);
    }

    public ProfileImageStatus getProfileImageStatus() {
        return ProfileImageStatus.from(profileImageStatus);
    }

    public StudentIdImageStatus getStudentIdImageStatus() {
        return StudentIdImageStatus.from(studentIdImageStatus);
    }
}
