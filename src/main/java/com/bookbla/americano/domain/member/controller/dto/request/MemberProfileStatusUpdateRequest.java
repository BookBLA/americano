package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.OpenKakaoRoomUrlStatus;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberProfileStatusUpdateRequest {

    @NotNull(message = "오픈카톡방 상태가 입력되지 않았습니다.")
    private String openKakaoRoomUrlStatus;

    public OpenKakaoRoomUrlStatus getOpenKakaoRoomUrlStatus() {
        return OpenKakaoRoomUrlStatus.from(openKakaoRoomUrlStatus);
    }

}
