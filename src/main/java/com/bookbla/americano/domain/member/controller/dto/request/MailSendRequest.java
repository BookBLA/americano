package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.MemberPolicy;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;
import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @NotNull(message = "학교 이메일이 입력되지 않았습니다.")
    private String schoolEmail;

    public MemberAuthDto toMemberAuthDto() {
        return MemberAuthDto.builder()
            .schoolEmail(schoolEmail)
            .build();
    }

}

