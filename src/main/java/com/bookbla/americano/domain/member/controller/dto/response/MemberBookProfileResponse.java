package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
}
