package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
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

    public ContactInfoResponse(Member member, List<MemberBook> memberBooks) {
        this.memberId = member.getId();
        this.memberName = member.getMemberProfile().getName();
        this.memberAge = getAge(member.getMemberProfile().getBirthDate());
        this.memberGender = member.getMemberProfile().getGender();
        this.memberProfileImageUrl = member.getMemberProfile().getProfileImageUrl();
        this.memberSchoolName = member.getSchool().getName();
        this.memberOpenKakaoRoomUrl = member.getMemberProfile().getOpenKakaoRoomUrl();
        List<String> imageUrls = new ArrayList<>();
        for (MemberBook i : memberBooks) {
            imageUrls.add(i.getBook().getImageUrl());
        }
        this.bookImageUrls = imageUrls;
    }

    private int getAge(LocalDate birthDay) {
        return Period.between(birthDay, LocalDate.now()).getYears();
    }
}
