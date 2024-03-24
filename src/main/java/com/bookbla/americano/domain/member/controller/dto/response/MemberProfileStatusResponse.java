package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileStatusResponse {

    private String profileImageUrlStatus;
    private String openKakaoRoomUrlStatus;
    private String studentIdImageStatus;

    public static MemberProfileStatusResponse from(MemberProfile memberProfile) {
        return MemberProfileStatusResponse.builder()
            .profileImageUrlStatus(memberProfile.getProfileImageStatus().name())
            .openKakaoRoomUrlStatus(memberProfile.getOpenKakaoRoomStatus().name())
            .studentIdImageStatus(memberProfile.getStudentIdImageStatus().name())
            .build();
    }
}
