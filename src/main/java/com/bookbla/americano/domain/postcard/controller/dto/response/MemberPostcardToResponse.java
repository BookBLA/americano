package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Gender memberGender;

    private DrinkType drinkType;

    private SmokeType smokeType;

    private ContactType contactType;

    private DateStyleType dateStyleType;

    private DateCostType dateCostType;

    private Mbti mbti;

    private JustFriendType justFriendType;

    private String memberSchoolName;

    private int quizScore;

    // 책 제목
    private List<String> bookTitles;

    // 독서 퀴즈 답
    private List<CorrectStatus> correctStatuses;

    // 개인 질문 답
    private String memberReplyContent;

    public MemberPostcardToResponse(long postcardId, long memberId, String memberName, String memberProfileImageUrl,
                                    int memberAge, Gender memberGender, DrinkType drinkType, SmokeType smokeType,
                                    ContactType contactType, DateStyleType dateStyleType, DateCostType dateCostType,
                                    Mbti mbti, JustFriendType justFriendType, String memberSchoolName, String memberReplyContent) {

        this.postcardId = postcardId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberProfileImageUrl = memberProfileImageUrl;
        this.memberAge = memberAge;
        this.memberGender = memberGender;
        this.drinkType = drinkType;
        this.smokeType = smokeType;
        this.contactType = contactType;
        this.dateStyleType = dateStyleType;
        this.dateCostType = dateCostType;
        this.mbti = mbti;
        this.justFriendType = justFriendType;
        this.memberSchoolName = memberSchoolName;
        this.memberReplyContent = memberReplyContent;
    }
}
