package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.member.enums.*;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 받은 엽서
public class MemberPostcardToResponse {

    private long postcardId;

    private long memberId;

    private String memberName;

    private String memberProfileImageUrl;

    private int memberAge;

    private String memberGender;

    private String drinkType;

    private String smokeType;

    private String contactType;

    private String dateStyleType;

    private String dateCostType;

    private String mbti;

    private String justFriendType;

    private String memberSchoolName;

    private int quizScore;

    private PostcardStatus postcardStatus;

    private String postcardImageUrl;

    // 책 제목
    private List<String> bookTitles;

    // 독서 퀴즈 답
    private List<CorrectStatus> correctStatuses;

    // 개인 질문 답
    private String memberReplyContent;

    private List<String> bookImageUrls;

    public MemberPostcardToResponse(long postcardId, long memberId, String memberName, String memberProfileImageUrl,
                                    int memberAge, Gender memberGender, DrinkType drinkType, SmokeType smokeType,
                                    ContactType contactType, DateStyleType dateStyleType, DateCostType dateCostType,
                                    Mbti mbti, JustFriendType justFriendType, String memberSchoolName, String memberReplyContent,
                                    PostcardStatus postcardStatus, String postcardImageUrl) {

        this.postcardId = postcardId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberProfileImageUrl = memberProfileImageUrl;
        this.memberAge = memberAge;
        this.memberGender = memberGender.name();
        this.drinkType = drinkType.getValue();
        this.smokeType = smokeType.getValue();
        this.contactType = contactType.getValue();
        this.dateStyleType = dateStyleType.getValue();
        this.dateCostType = dateCostType.getValue();
        this.mbti = mbti.name();
        this.justFriendType = justFriendType.getValue();
        this.memberSchoolName = memberSchoolName;
        this.memberReplyContent = memberReplyContent;
        this.postcardStatus = postcardStatus;
        this.postcardImageUrl = postcardImageUrl;
    }
}
