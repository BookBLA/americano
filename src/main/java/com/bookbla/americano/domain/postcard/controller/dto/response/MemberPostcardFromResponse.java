package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
// 보낸 엽서
public class MemberPostcardFromResponse {

    // Member 정보
    private long memberId;

    private String memberName;

    private int memberAge;

    private Gender memberGender;

    private String memberSchoolName;

    private String memberProfileImageUrl;

    private String memberOpenKakaoRoomUrl;

    // 책 정보
    private String representativeBookTitle;

    private List<String> representativeBookAuthor;

    private List<String> bookImageUrls;

    // 엽서 정보
    private long postcardId;

    private PostcardStatus postcardStatus;

    public MemberPostcardFromResponse(long memberId, String memberName, LocalDate memberBirthDate, Gender memberGender,
                                      String memberSchoolName, String memberProfileImageUrl, String memberOpenKakaoRoomUrl,
                                      long postcardId, PostcardStatus postcardStatus) {
        this.memberId = memberId;
//        if(postcardStatus.equals(PostcardStatus.ACCEPT)){
            this.memberName = memberName;
//        } else {
//            this.memberName = transformMemberName(memberName);
//        }
        this.memberAge = getAge(memberBirthDate);
        this.memberGender = memberGender;
        this.memberSchoolName = memberSchoolName;
        this.memberProfileImageUrl = memberProfileImageUrl;
        this.memberOpenKakaoRoomUrl = memberOpenKakaoRoomUrl;
        this.postcardId = postcardId;
        this.postcardStatus = postcardStatus;
    }

    private String transformMemberName(String name){
        if(name == null || name.isEmpty())
            return "";
        char lastName = name.charAt(0);
        return lastName +
                "O".repeat(name.length() - 1);
    }

    private int getAge(LocalDate birthDay) {
        return Period.between(birthDay, LocalDate.now()).getYears();
    }
}
