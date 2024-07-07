package com.bookbla.americano.domain.school.controller.dto.response;

import com.bookbla.americano.base.utils.CalculateUtil;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SchoolInvitationResponse {

    private final String schoolStatus;
    private final String schoolName;
    private final String invitationCode;
    private final int currentMemberCount;
    private final int goalMemberCount;
    private final int percentage;

    public static SchoolInvitationResponse of(Member member, School school) {
        int currentMemberCount = school.validMemberCounts();
        int goalMemberCount = school.getOpenStandard();
        return new SchoolInvitationResponse(
                school.getSchoolStatus().name(),
                school.getName(),
                member.getInvitationCode(),
                currentMemberCount,
                goalMemberCount,
                CalculateUtil.calculatePercentage(currentMemberCount, goalMemberCount)
        );
    }
}
