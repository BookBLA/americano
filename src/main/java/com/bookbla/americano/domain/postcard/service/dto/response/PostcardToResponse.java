package com.bookbla.americano.domain.postcard.service.dto.response;

import com.bookbla.americano.domain.member.enums.*;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import lombok.*;

import java.time.LocalDate;

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

    private DrinkType drinkType;

    private SmokeType smokeType;

    private ContactType contactType;

    private DateStyleType dateStyleType;

    private DateCostType dateCostType;

    private Mbti mbti;

    private JustFriendType justFriendType;

    // 엽서 상태
    private PostcardStatus postcardStatus;

    // 엽서 이미지
    private String postcardImageUrl;

    // 책 제목
    private String bookTitle;

    private String bookImageUrl;

    // 독서 퀴즈 답
    private CorrectStatus correctStatus;

    // 개인 질문 답
    private String memberReplyContent;

}
