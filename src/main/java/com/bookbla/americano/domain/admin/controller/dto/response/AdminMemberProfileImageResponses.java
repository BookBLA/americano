package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Getter
public class AdminMemberProfileImageResponses {

    private final List<AdminMemberProfileImageResponse> adminMemberProfileImageResponses;

    @AllArgsConstructor
    public static class AdminMemberProfileImageResponse {

        private final Long memberProfileId;
        private final String name;
        private final LocalDate birthDate;
        private final String gender;
        private final String schoolName;
        private final String phoneNumber;
        private final String profileImageUrl;
        private final String profileImageStatus;

        public static AdminMemberProfileImageResponse from(MemberProfile memberProfile) {
            return new AdminMemberProfileImageResponse(
                    memberProfile.getId(),
                    memberProfile.getName(),
                    memberProfile.getBirthDate(),
                    memberProfile.getGender().name(),
                    memberProfile.getSchoolName(),
                    memberProfile.getPhoneNumber(),
                    memberProfile.getProfileImageUrl(),
                    memberProfile.getProfileImageStatus().name()
            );
        }
    }

    public static AdminMemberProfileImageResponses from(List<MemberProfile> pendingMemberProfiles) {
        List<AdminMemberProfileImageResponse> pendingMemberProfileResponses = pendingMemberProfiles.stream()
                .map(AdminMemberProfileImageResponse::from)
                .collect(toList());
        return new AdminMemberProfileImageResponses(pendingMemberProfileResponses);
    }
}
