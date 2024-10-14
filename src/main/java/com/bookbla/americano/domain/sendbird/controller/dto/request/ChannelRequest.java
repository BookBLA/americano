package com.bookbla.americano.domain.sendbird.controller.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Builder
public class ChannelRequest {

    @NotNull(message = "채팅(엽서) 보내는 회원이 입력되지 않았습니다.")
    private Long sendMemberId;

    @NotNull(message = "채팅(엽서) 보내는 회원의 이름이 입력되지 않았습니다.")
    private String sendMemberName;

    @NotNull(message = "채팅(엽서) 인사말이 입력되지 않았습니다.")
    private String sendMemberReview;

    @NotNull(message = "채팅(엽서) 받는 회원이 입력되지 않았습니다.")
    private Long targetMemberId;

    @NotNull(message = "채팅(엽서) 보내는 회원의 책이 입력되지 않았습니다.")
    private Long targetMemberBookId;

    @NotNull(message = "채팅 URL이 입력되지 않았습니다.")
    private String channelUrl;
}
