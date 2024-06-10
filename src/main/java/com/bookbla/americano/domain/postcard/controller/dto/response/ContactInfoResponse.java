package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ContactInfoResponse {
    private long memberId;

    private String memberName;

    private int memberAge;

    private Gender memberGender;

    private String memberProfileImageUrl;

    private String memberSchoolName;

    private String memberOpenKakaoRoomUrl;

    private List<String> bookImageUrls;

    public ContactInfoResponse(MemberProfileResponse memberProfileResponse, MemberBookReadResponses memberBookReadResponses) {
        this.memberId = memberProfileResponse.getMemberId();
        this.memberName = memberProfileResponse.getName();
        this.memberAge = getAge(LocalDate.parse(memberProfileResponse.getBirthDate()));
        this.memberGender = Gender.from(memberProfileResponse.getGender());
        this.memberProfileImageUrl = memberProfileResponse.getProfileImageUrl();
        this.memberSchoolName = memberProfileResponse.getSchoolName();
        this.memberOpenKakaoRoomUrl = memberProfileResponse.getOpenKakaoRoomUrl();
        List<String> imageUrls = new ArrayList<>();
        for (MemberBookReadResponses.MemberBookReadResponse i : memberBookReadResponses.getMemberBookReadResponses()) {
            imageUrls.add(i.getThumbnail());
        }
        this.bookImageUrls = imageUrls;
    }

    private int getAge(LocalDate birthDay) {
        return Period.between(birthDay, LocalDate.now()).getYears();
    }
}
