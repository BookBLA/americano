package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
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

    private String heightType;

    private String memberSchoolName;

    private LocalDateTime receivedTime;

    private PostcardStatus postcardStatus;

    private String postcardImageUrl;

    public MemberPostcardToResponse(PostcardToResponse i) {

        this.postcardId = i.getPostcardId();
        this.memberId = i.getMemberId();
        this.memberName = i.getMemberName();
        this.memberProfileImageUrl = i.getMemberProfileImageUrl();
        this.memberAge = getAge(i.getMemberBirthDate());
        this.memberGender = i.getMemberGender().name();
        this.smokeType = i.getSmokeType().getDetailValue();
        this.mbti = i.getMbti().name();
        this.memberSchoolName = i.getMemberSchoolName();
        this.receivedTime = i.getReceivedTime();
        this.postcardStatus = i.getPostcardStatus();
        this.postcardImageUrl = i.getPostcardImageUrl();
    }

    private int getAge(LocalDate birthDay) {
        return LocalDate.now().getYear() - birthDay.getYear() + 1;
    }
}
