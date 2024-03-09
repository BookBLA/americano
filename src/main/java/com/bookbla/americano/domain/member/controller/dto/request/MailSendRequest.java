package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MailSendRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreedStatuses {
        private Boolean adAgreementPolicy;
    }

    @NotNull(message = "약관의 동의여부가 입력되지 않았습니다.")
    private AgreedStatuses agreedStatuses;

    @NotNull(message = "성별이 입력되지 않았습니다.")
    private String gender;

    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private LocalDate birthDate;

    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;

    @NotNull(message = "전화 번호가 입력되지 않았습니다.")
    private String phoneNumber;

    @NotNull(message = "학교가 입력되지 않았습니다.")
    private String schoolName;

    @NotNull(message = "학생증 이미지 주소가 입력되지 않았습니다.")
    private String studentIdImageUrl;

    @NotNull(message = "학교 이메일이 입력되지 않았습니다.")
    private String schoolEmail;

    public MemberAuthDto toMemberAuthDto() {
        return MemberAuthDto.builder()
            .schoolEmail(schoolEmail)
            .phoneNumber(phoneNumber)
            .studentIdImageUrl(studentIdImageUrl)
            .build();
    }

    public MemberProfileDto toMemberProfileDto() {
        return MemberProfileDto.builder()
            .gender(Gender.from(gender))
            .birthDate(birthDate)
            .name(name)
            .schoolName(schoolName)
            .build();
    }

}

