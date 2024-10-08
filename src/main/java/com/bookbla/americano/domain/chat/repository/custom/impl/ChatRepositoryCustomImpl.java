package com.bookbla.americano.domain.chat.repository.custom.impl;

import com.bookbla.americano.domain.chat.repository.custom.ChatRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.QChat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Transactional
    public void updateIsReadByChatRoomId(Long chatRoomId, Long memberId, Boolean isRead) {
        QChat qChat = QChat.chat;

        queryFactory.update(qChat)
                .set(qChat.isRead, isRead)
                .where(qChat.chatRoom.id.eq(chatRoomId))
                .where(qChat.sender.id.ne(memberId))
                .execute();
    }
}
