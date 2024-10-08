package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRoomResponse {

    MemberResponse otherMember;

    PostCardResponse postcard;

    Long id;

    int unreadCount;

    String lastChat;

    LocalDateTime lastChatTime;

    Boolean isAlert;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class PostCardResponse {
        private Long postcardId;
        private String imageUrl;
        private String message;
        private LocalDateTime createdAt;
        private PostcardStatus status;
        private Long senderId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class MemberResponse {
        private Long memberId;
        private String name;
        private String profileImageUrl;
        private Gender profileGender;
        private Mbti mbti;
        private SmokeType smokeType;
        private Integer height;
        private String schoolName;
    }
}
