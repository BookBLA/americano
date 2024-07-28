package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String memberOpenKakaoRoomUrl;

    private int memberAge;

    private String memberGender;

    private String drinkType;

    private String smokeType;

    private String contactType;

    private String dateStyleType;

    private String dateCostType;

    private String mbti;

    private String justFriendType;

    private String heightType;

    private String memberSchoolName;

    private int quizScore;

    private LocalDateTime receivedTime;

    private PostcardStatus postcardStatus;

    private String postcardImageUrl;

    // 책 제목
    private List<String> bookTitles;

    // 독서 퀴즈 답
    private List<CorrectStatus> correctStatuses;

    // 개인 질문 답
    private String memberReplyContent;

    private List<String> bookImageUrls;

    public MemberPostcardToResponse(PostcardToResponse i) {

        this.postcardId = i.getPostcardId();
        this.memberId = i.getMemberId();
        this.memberName = i.getMemberName();
        this.memberProfileImageUrl = i.getMemberProfileImageUrl();
        this.memberAge = getAge(i.getMemberBirthDate());
        this.memberGender = i.getMemberGender().name();
        this.drinkType = i.getDrinkType().getDetailValue();
        this.smokeType = i.getSmokeType().getDetailValue();
        this.contactType = i.getContactType().getDetailValue();
        this.dateStyleType = i.getDateStyleType().getDetailValue();
        this.dateCostType = i.getDateCostType().getDetailValue();
        this.mbti = i.getMbti().name();
        this.justFriendType = i.getJustFriendType().getDetailValue();
        this.heightType = i.getHeightType().getDetailValue();
        this.memberSchoolName = i.getMemberSchoolName();
        this.memberReplyContent = i.getMemberReplyContent();
        this.receivedTime = i.getReceivedTime();
        this.postcardStatus = i.getPostcardStatus();
        this.postcardImageUrl = i.getPostcardImageUrl();
        this.memberOpenKakaoRoomUrl = i.getMemberKakaoRoomUrl();
    }

    private int getAge(LocalDate birthDay) {
        return Period.between(birthDay, LocalDate.now()).getYears();
    }
}
