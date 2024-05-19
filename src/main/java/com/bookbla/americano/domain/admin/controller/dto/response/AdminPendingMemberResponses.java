package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AdminPendingMemberResponses {

    private final long totalCount;
    private final List<AdminPendingMemberResponse> data;

    public static AdminPendingMemberResponses from(long count, List<Member> members) {
        List<AdminPendingMemberResponse> data = members.stream()
                .map(AdminPendingMemberResponse::from)
                .collect(Collectors.toList());
        return new AdminPendingMemberResponses(count, data);
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminPendingMemberResponse {

        private final Long memberId;
        private final String name;
        private final String gender;
        private final String profileImageUrl;
        private final String openKakaoRoomUrl;
        private final String schoolName;
        private final String major;
        private final String studentId;
        private final String studentIdImageUrl;
        private final String profileImageStatus;
        private final String openKakaoRoomStatus;
        private final String studentIdImageStatus;

        public static AdminPendingMemberResponse from(Member member) {
            MemberProfile memberProfile = member.getMemberProfile();

            return AdminPendingMemberResponse.builder()
                    .memberId(member.getId())
                    .name(memberProfile.getName())
                    .gender(memberProfile.getGender().name())
                    .profileImageUrl(memberProfile.getProfileImageUrl())
                    .openKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
                    .schoolName(memberProfile.getSchoolName())
                    .major(memberProfile.getMajor())
                    .studentId(memberProfile.getStudentNumber())
                    .studentIdImageUrl(memberProfile.getStudentIdImageUrl())
                    .profileImageStatus(memberProfile.getProfileImageStatus().name())
                    .openKakaoRoomStatus(memberProfile.getOpenKakaoRoomStatus().name())
                    .studentIdImageStatus(memberProfile.getStudentIdImageStatus().name())
                    .build();
        }
    }
}
