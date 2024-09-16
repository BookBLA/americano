package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.LAZY)
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

    public String getSmokeTypeName() {
        if (Objects.isNull(smokeType)) {
            return "등록되지 않음";
        }
        return getSmokeType().name();
    }
}
