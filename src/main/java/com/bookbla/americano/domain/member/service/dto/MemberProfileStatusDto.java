package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomUrlStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageUrlStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberProfileStatusDto {

    private ProfileImageUrlStatus profileImageUrlStatus;
    private OpenKakaoRoomUrlStatus openKakaoRoomUrlStatus;
}
