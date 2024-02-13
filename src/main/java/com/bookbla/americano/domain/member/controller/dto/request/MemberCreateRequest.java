package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.enums.Gender;
import com.bookbla.americano.domain.member.service.dto.MemberDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberCreateRequest {

    @NotNull(message = "성별이 입력되지 않았습니다.")
    private Gender gender;

    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private LocalDate birthDate;

    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @NotNull(message = "전화번호가 입력되지 않았습니다.")
    private String phoneNumber;

    private String schoolName;

    private String studentIdImageUrl;

    private String schoolEmail;

    public MemberDto toDto(Member member) {
        return MemberDto.builder()
            .member(member)
            .gender(gender)
            .birthDate(birthDate)
            .name(name)
            .phoneNumber(phoneNumber)
            .schoolName(schoolName)
            .studentIdImageUrl(studentIdImageUrl)
            .schoolEmail(schoolEmail)
            .build();
    }

}
