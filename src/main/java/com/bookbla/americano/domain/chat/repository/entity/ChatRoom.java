package com.bookbla.americano.domain.chat.repository.entity;


import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 Postcard 로 만들어진 ChatRoom 인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postcard_id")
    private Postcard postcard;

    @Column
    private Boolean isAlert;

    @Column
    private String lastChat;

    @Column
    private DateTime lastChatTime;


}
