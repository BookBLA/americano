package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberBookProfileResponse {

    private long memberId;

    private long bookId;

    private String memberName;

    private int memberAge;

    private Gender memberGender;

    private String memberSchoolName;

    private String bookName;

    private String bookImageUrl;

    private boolean bookIsRepresentative;

    public MemberBookProfileResponse(BookProfileResponse bookProfileResponse) {
        this.memberId = bookProfileResponse.getMemberId();
        this.bookId = bookProfileResponse.getBookId();
        this.memberName = transformMemberName(bookProfileResponse.getMemberName());
        this.memberAge = getAge(bookProfileResponse.getMemberBirthDate());
        this.memberGender = bookProfileResponse.getMemberGender();
        this.memberSchoolName = bookProfileResponse.getMemberSchoolName();
        this.bookName = bookProfileResponse.getBookName();
        this.bookImageUrl = bookProfileResponse.getBookImageUrl();
        this.bookIsRepresentative = bookProfileResponse.isBookIsRepresentative();
    }

    private int getAge(LocalDate birthDay) {
        return Period.between(birthDay, LocalDate.now()).getYears();
    }

    private String transformMemberName(String name){
        if(name == null || name.isEmpty())
            return "";
        char lastName = name.charAt(0);
        return lastName +
                "O".repeat(name.length() - 1);
    }
}
