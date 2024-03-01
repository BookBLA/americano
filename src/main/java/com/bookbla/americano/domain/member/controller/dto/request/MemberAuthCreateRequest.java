package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAuthCreateRequest {

    @NotNull(message = "학교 이메일이 입력되지 않았습니다.")
    private String schoolEmail;

    @NotNull(message = "전화번호가 입력되지 않았습니다.")
    private String phoneNumber;

    @NotNull(message = "학생증 이미지 주소가 입력되지 않았습니다.")
    private String studentIdImageUrl;

    public MemberAuthDto toDto(Member member, String emailVerifyCode) {
        return MemberAuthDto.builder()
            .member(member)
            .schoolEmail(schoolEmail)
            .emailVerifyCode(emailVerifyCode)
            .phoneNumber(phoneNumber)
            .studentIdImageUrl(studentIdImageUrl)
            .build();
    }

}
