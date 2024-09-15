package com.bookbla.americano.domain.chat.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    private String content;

    private DateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;
}
