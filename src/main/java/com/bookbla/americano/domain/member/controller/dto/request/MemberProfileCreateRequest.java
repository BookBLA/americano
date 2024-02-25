package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberProfileCreateRequest {

    @NotNull(message = "성별이 입력되지 않았습니다.")
    private Gender gender;

    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private LocalDate birthDate;

    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @NotNull(message = "학교가 입력되지 않았습니다.")
    private String schoolName;

    public MemberProfileDto toDto(Member member) {
        return MemberProfileDto.builder()
            .gender(gender)
            .birthDate(birthDate)
            .name(name)
            .schoolName(schoolName)
            .build();
    }
}
