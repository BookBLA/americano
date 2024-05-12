package com.bookbla.americano.domain.postcard.service.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostcardFromResponse {
    // Member 정보
    private long memberId;

    private String memberName;

    private LocalDate memberBirthDate;

    private Gender memberGender;

    private String memberSchoolName;

    private String memberProfileImageUrl;

    private String memberOpenKakaoRoomUrl;

    // 엽서 정보
    private PostcardStatus postcardStatus;
}
