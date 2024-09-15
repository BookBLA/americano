package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatRoomResponse {

    MemberResponse otherMember;

    PostCardResponse postcard;

    Long id;

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    public static class PostCardResponse {
        private Long id;
        private PostcardType type;
        private String imageUrl;
        private String message;
        private PostcardStatus status;

        public static PostCardResponse of(Postcard postcard) {
            return PostCardResponse.builder()
                    .id(postcard.getId())
                    .type(postcard.getPostcardType())
                    .imageUrl(postcard.getImageUrl())
                    .message(postcard.getMessage())
                    .status(postcard.getPostcardStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor
    public static class MemberResponse {
        private Long id;
        private String name;
        private String profileImageUrl;
        private Gender profileGender;
    }
}
