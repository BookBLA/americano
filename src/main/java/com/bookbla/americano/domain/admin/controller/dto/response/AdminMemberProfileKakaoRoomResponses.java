package com.bookbla.americano.domain.admin.controller.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Getter
public class AdminMemberProfileKakaoRoomResponses {

    private final List<AdminMemberProfileKakaoRoomResponse> adminMemberProfileKakaoRoomResponses;

    @AllArgsConstructor
    public static class AdminMemberProfileKakaoRoomResponse {

        private final Long memberProfileId;
        private final String name;
        private final LocalDate birthDate;
        private final String gender;
        private final String schoolName;
        private final String phoneNumber;
        private final String openKakaoRoomUrl;
        private final String openKakaoRoomStatus;

        public static AdminMemberProfileKakaoRoomResponse from(MemberProfile memberProfile) {
            return new AdminMemberProfileKakaoRoomResponse(
                    memberProfile.getId(),
                    memberProfile.getName(),
                    memberProfile.getBirthDate(),
                    memberProfile.getGender().name(),
                    memberProfile.getSchoolName(),
                    memberProfile.getPhoneNumber(),
                    memberProfile.getOpenKakaoRoomUrl(),
                    memberProfile.getOpenKakaoRoomStatus().name()
            );
        }
    }

    public static AdminMemberProfileKakaoRoomResponses from(List<MemberProfile> pendingMemberProfiles) {
        List<AdminMemberProfileKakaoRoomResponse> pendingMemberProfileResponses = pendingMemberProfiles.stream()
                .map(AdminMemberProfileKakaoRoomResponse::from)
                .collect(toList());
        return new AdminMemberProfileKakaoRoomResponses(pendingMemberProfileResponses);
    }
}
