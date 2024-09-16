package com.bookbla.americano.domain.chat.repository.custom.impl;

import com.bookbla.americano.domain.chat.repository.custom.MemberChatRoomRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QMemberChatRoom;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberChatRoomRepositoryCustomImpl implements MemberChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberChatRoom> findByMemberIdAndPostcardId(Long memberId, Long postcardId) {
        QPostcard qPostcard = QPostcard.postcard;
        QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
        QChatRoom qChatRoom = QChatRoom.chatRoom;

        return queryFactory.select(qMemberChatRoom)
                .from(qPostcard)
                .innerJoin(qChatRoom).on(qChatRoom.postcard.eq(qPostcard))
                .innerJoin(qMemberChatRoom).on(qMemberChatRoom.chatRoom.eq(qChatRoom))
                .where(qMemberChatRoom.chatRoom.eq(qChatRoom)
                        .and(qMemberChatRoom.member.id.eq(memberId)))
                .fetch();
    }
}
