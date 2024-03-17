package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomUrlStatus;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberProfileDto {

    private Gender gender;
    private LocalDate birthDate;
    private String name;
    private String schoolName;
    private String profileImageUrl;
    private String openKakaoRoomUrl;

    public MemberProfile toEntity(Member member) {
        return MemberProfile.builder()
            .member(member)
            .gender(gender)
            .birthDate(birthDate)
            .name(name)
            .schoolName(schoolName)
            .profileImageUrl(profileImageUrl)
            .openKakaoRoomUrl(openKakaoRoomUrl)
            .openKakaoRoomUrlStatus(OpenKakaoRoomUrlStatus.PENDING)
            .build();
    }
}
