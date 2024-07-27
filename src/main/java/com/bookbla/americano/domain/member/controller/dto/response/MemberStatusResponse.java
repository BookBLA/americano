package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberStatusResponse {

    private String memberStatus;
    private String school;
    private String schoolStatus;

    public static MemberStatusResponse from(Member member, School school) {
        return MemberStatusResponse.builder()
                .memberStatus(member.getMemberStatus().getValue())
                .school(school.getName())
                .schoolStatus(school.getSchoolStatus().name())
                .build();
    }

}
