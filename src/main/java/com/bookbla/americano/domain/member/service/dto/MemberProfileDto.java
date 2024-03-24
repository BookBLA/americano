package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
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

    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private String schoolName;
    private String phoneNumber;
    private String profileImageUrl;
    private String openKakaoRoomUrl;
    private String studentIdImageUrl;

    public MemberProfile toEntity(Member member) {
        return MemberProfile.builder()
            .member(member)
            .name(name)
            .birthDate(birthDate)
            .gender(gender)
            .schoolName(schoolName)
            .phoneNumber(phoneNumber)
            .profileImageUrl(profileImageUrl)
            .openKakaoRoomUrl(openKakaoRoomUrl)
            .studentIdImageUrl(studentIdImageUrl)
            .build();
    }
}
