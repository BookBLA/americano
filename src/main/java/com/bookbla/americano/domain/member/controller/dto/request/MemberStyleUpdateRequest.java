package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.ProfileImageType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberStyleUpdateRequest {

    @NotNull(message = "mbti가 입력되지 않았습니다.")
    private String mbti;

    @NotNull(message = "흡연 타입이 입력되지 않았습니다.")
    private String smokeType;

    @Max(value = 230, message = "최대 키는 230까지 입력 가능합니다.")
    @Min(value = 140, message = "최소 키는 140까지 입력 가능합니다.")
    private Integer height;

    @NotNull(message = "프로필 사진 id가 입력되지 않았습니다.")
    private Integer profileImageTypeId;

    public Mbti getMbti() {
        return Mbti.from(mbti);
    }

    public SmokeType getSmokeType() {
        return SmokeType.from(smokeType);
    }

    public Integer getHeight() {
        return height;
    }

    public ProfileImageType getProfileImageType() {
        return ProfileImageType.fromId(profileImageTypeId);
    }
}
