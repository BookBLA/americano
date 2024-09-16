package com.bookbla.americano.domain.chat.repository.entity;


import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.member.repository.entity.BooleanToYNConverter;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int unreadCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column
    @Builder.Default
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAlert = Boolean.TRUE;


    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
