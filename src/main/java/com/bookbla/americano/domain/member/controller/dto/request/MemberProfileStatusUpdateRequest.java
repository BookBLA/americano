package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomUrlStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageUrlStatus;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
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
    private String profileImageUrlStatus;

    @NotNull(message = "오픈카톡방 상태가 입력되지 않았습니다.")
    private String openKakaoRoomUrlStatus;

    public ProfileImageUrlStatus getProfileImageUrlStatus() {
        return ProfileImageUrlStatus.from(profileImageUrlStatus);
    }

    public OpenKakaoRoomUrlStatus getOpenKakaoRoomUrlStatus() {
        return OpenKakaoRoomUrlStatus.from(openKakaoRoomUrlStatus);
    }

    public MemberProfileStatusDto toDto() {
        return MemberProfileStatusDto.builder()
            .profileImageUrlStatus(getProfileImageUrlStatus())
            .openKakaoRoomUrlStatus(getOpenKakaoRoomUrlStatus())
            .build();
    }

}
