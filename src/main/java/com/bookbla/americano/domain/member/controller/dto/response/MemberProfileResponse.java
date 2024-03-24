package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileResponse {

    private Long memberId;
    private Long memberProfileId;
    private String name;
    private String birthDate;
    private String schoolName;
    private String gender;
    private String profileImageUrl;
    private String openKakaoRoomUrl;
    private String studentIdImageUrl;

    public static MemberProfileResponse from(Member member, MemberProfile memberProfile) {
        return MemberProfileResponse.builder()
            .memberId(member.getId())
            .memberProfileId(memberProfile.getId())
            .birthDate(memberProfile.getBirthDate().toString())
            .schoolName(memberProfile.getSchoolName())
            .profileImageUrl(memberProfile.getProfileImageUrl())
            .openKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
            .studentIdImageUrl(memberProfile.getStudentIdImageUrl())
            .build();
    }
}
