package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
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
    private String schoolEmail;
    private String phoneNumber;
    private String profileImageUrl;
    private String openKakaoRoomUrl;
    private String studentIdImageUrl;

    public MemberProfile toMemberProfile() {
        return MemberProfile.builder()
            .name(name)
            .birthDate(birthDate)
            .gender(gender)
            .schoolName(schoolName)
            .schoolEmail(schoolEmail)
            .phoneNumber(phoneNumber)
            .build();
    }
    public String toMemberVerifyDescription() {
        return "name: " + getName() +
                ", schoolName: " + getSchoolName() +
                ", birthDate: " + getBirthDate().format(DateTimeFormatter.BASIC_ISO_DATE) +
                ", gender: " + gender.name();
    }
}
