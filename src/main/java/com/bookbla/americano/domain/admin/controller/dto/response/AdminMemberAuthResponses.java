package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AdminMemberAuthResponses {

    private final List<AdminMemberAuthResponse> data;

    public static AdminMemberAuthResponses from(List<Member> members) {
        List<AdminMemberAuthResponse> adminMemberAuthResponses = members.stream()
                .map(AdminMemberAuthResponse::from)
                .collect(Collectors.toList());

        return new AdminMemberAuthResponses(adminMemberAuthResponses);
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberAuthResponse {

        private final Long memberId;
        private final String name;
        private final String openKakaoRoomUrl;
        private final String kakaoRoomUrlStatus;
        private final String profileImageUrl;
        private final String profileImageUrlStatus;
        private final String studentIdImageUrl;
        private final String studentIdImageUrlStatus;
        private final String authStatus;

        public static AdminMemberAuthResponse from(Member member) {
            MemberProfile memberProfile = member.getMemberProfile();

            return AdminMemberAuthResponse.builder()
                    .memberId(member.getId())
                    .name(memberProfile.getName())
                    .openKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
                    .kakaoRoomUrlStatus(memberProfile.getOpenKakaoRoomStatus().name())
                    .profileImageUrl(memberProfile.getProfileImageUrl())
                    .profileImageUrlStatus(memberProfile.getProfileImageStatus().name())
                    .studentIdImageUrl(memberProfile.getStudentIdImageUrl())
                    .studentIdImageUrlStatus(memberProfile.getStudentIdImageStatus().name())
                    .authStatus(member.getMemberStatus().name())
                    .build();
        }
    }
}
