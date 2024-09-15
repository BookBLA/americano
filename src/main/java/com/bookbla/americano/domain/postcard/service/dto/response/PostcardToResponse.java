package com.bookbla.americano.domain.postcard.service.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 받은 엽서
public class PostcardToResponse {

    private long postcardId;

    // Member 정보
    private long memberId;

    private String memberName;

    private LocalDate memberBirthDate;

    private Gender memberGender;

    private String memberSchoolName;

    private String memberProfileImageUrl;

    private SmokeType smokeType;

    private Mbti mbti;

    // 엽서 받은 시간
    private LocalDateTime receivedTime;

    // 엽서 상태
    private PostcardStatus postcardStatus;

    // 엽서 이미지
    private String postcardImageUrl;

}
