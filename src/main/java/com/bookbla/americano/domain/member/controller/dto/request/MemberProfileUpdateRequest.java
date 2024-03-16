package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.enums.Gender;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberProfileUpdateRequest {

    @NotNull(message = "이름이 입력되지 않았습니다.")
    private String name;
    
    @NotNull(message = "생년월일이 입력되지 않았습니다.")
    private String birthDate;

    @NotNull(message = "학교명이 입력되지 않았습니다.")
    private String schoolName;
    
    @NotNull(message = "성별이 입력되지 않았습니다.")
    private String gender;

    @NotNull(message = "카톡방 링크가 입력되지 않았습니다.")
    private String openKakaoRoomUrl;

    public LocalDate getBirthDate() {
        return LocalDate.parse(birthDate);
    }

    public Gender getGender() {
        return Gender.from(gender);
    }
    
}
