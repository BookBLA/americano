package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.ProfileImageType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberStyle {

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Enumerated(EnumType.STRING)
    private SmokeType smokeType;

    private Integer height;

    @Enumerated(EnumType.STRING)
    private ProfileImageType profileImageType;

    public MemberStyle updateHeight(int height) {
        this.height = height;
        return this;
    }

    public MemberStyle updateMbti(Mbti mbti) {
        this.mbti = mbti;
        return this;
    }

    public MemberStyle updateSmokeType(SmokeType smokeType) {
        this.smokeType = smokeType;
        return this;
    }

    public MemberStyle updateProfileImageType(ProfileImageType profileImageType) {
        this.profileImageType = profileImageType;
        return this;
    }
}
