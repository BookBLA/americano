package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Getter
public class AdminMemberProfileStudentIdImageResponses {

    private final List<AdminMemberProfileStudentIdImageResponse> adminMemberProfileStudentIdImageResponses;

    @AllArgsConstructor
    public static class AdminMemberProfileStudentIdImageResponse {

        private final Long memberProfileId;
        private final String name;
        private final String nickname;
        private final LocalDate birthDate;
        private final String gender;
        private final String phoneNumber;
        private final String schoolName;
        private final String studentNumber;
        private final String major;
        private final String studentIdImageUrl;
        private final String studentIdImageStatus;

        public static AdminMemberProfileStudentIdImageResponse from(MemberProfile memberProfile) {
            return new AdminMemberProfileStudentIdImageResponse(
                    memberProfile.getId(),
                    memberProfile.getName(),
                    memberProfile.getNickname(),
                    memberProfile.getBirthDate(),
                    memberProfile.getGender().name(),
                    memberProfile.getPhoneNumber(),
                    memberProfile.getSchoolName(),
                    memberProfile.getStudentNumber(),
                    memberProfile.getMajor(),
                    memberProfile.getStudentIdImageUrl(),
                    memberProfile.getStudentIdImageStatus().name()
            );
        }
    }

    public static AdminMemberProfileStudentIdImageResponses from(List<MemberProfile> pendingMemberProfiles) {
        List<AdminMemberProfileStudentIdImageResponse> pendingMemberProfileResponses = pendingMemberProfiles.stream()
                .map(AdminMemberProfileStudentIdImageResponse::from)
                .collect(toList());
        return new AdminMemberProfileStudentIdImageResponses(pendingMemberProfileResponses);
    }

}
