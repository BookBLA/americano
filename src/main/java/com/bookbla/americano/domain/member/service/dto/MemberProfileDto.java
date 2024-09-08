package com.bookbla.americano.domain.member.service.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
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

    public MemberProfile toMemberProfile() {
        return MemberProfile.builder()
                .name(name)
                .birthDate(birthDate)
                .gender(gender)
                .schoolEmail(schoolEmail)
                .build();
    }
}
