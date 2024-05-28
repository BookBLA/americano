package com.bookbla.americano.domain.admin.controller.dto.response;

import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public class AdminMemberProfileImageResponses {

    private final long totalCount;
    private final List<AdminMemberProfileImageResponse> datas;

    public static AdminMemberProfileImageResponses from(long totalCount, List<MemberVerify> memberVerifies) {
        List<AdminMemberProfileImageResponse> pendingMemberProfileResponses = memberVerifies.stream()
                .map(AdminMemberProfileImageResponse::from)
                .collect(toList());

        return new AdminMemberProfileImageResponses(totalCount, pendingMemberProfileResponses);
    }

    @AllArgsConstructor
    @Builder
    @Getter
    static class AdminMemberProfileImageResponse {

        private final Long memberVerifyId;
        private final Long memberId;
        private final String gender;
        private final String profileImageUrl;

        public static AdminMemberProfileImageResponse from(MemberVerify memberVerify) {
            return AdminMemberProfileImageResponse.builder()
                    .memberVerifyId(memberVerify.getId())
                    .memberId(memberVerify.getMemberId())
                    .gender(memberVerify.getDescription())
                    .profileImageUrl(memberVerify.getContents())
                    .build();
        }
    }
}