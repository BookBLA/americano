package com.bookbla.americano.domain.member.service.dto;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.member.repository.entity.enums.Gender;
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

    private Member member;
    private Gender gender;
    private LocalDate birthDate;
    private String name;
    private String schoolName;

    public MemberProfile toEntity() {
        return MemberProfile.builder()
            .member(member)
            .gender(gender)
            .birthDate(birthDate)
            .name(name)
            .schoolName(schoolName)
            .build();
    }
}
