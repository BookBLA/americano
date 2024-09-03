package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileResponse {

    private Long memberId;
    private String name;
    private String birthDate;
    private String schoolName;
    private String schoolEmail;
    private String phoneNumber;
    private String gender;
//    private String openKakaoRoomUrl;

    public static MemberProfileResponse from(Member member, MemberProfile memberProfile) {
        return MemberProfileResponse.builder()
                .memberId(member.getId())
                .name(memberProfile.getName())
                .birthDate(memberProfile.getBirthDate().toString())
                .schoolName(member.getSchool().getName())
                .schoolEmail(memberProfile.getSchoolEmail())
                .phoneNumber(memberProfile.getPhoneNumber())
                .gender(memberProfile.getGender().name())
//                .openKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
                .build();
    }
}
