package com.bookbla.americano.domain.chat.repository.custom.impl;

import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
import com.bookbla.americano.domain.chat.repository.custom.ChatRoomRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QMemberChatRoom;
import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.member.repository.entity.QMemberProfile;
import com.bookbla.americano.domain.member.repository.entity.QMemberStyle;
import com.bookbla.americano.domain.member.repository.entity.QProfileImageType;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

//    @Override
//    public List<ChatRoom> findByMemberId(Long memberId) {
//        QMember qMember = QMember.member;
//        QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
//        QChatRoom qChatRoom = QChatRoom.chatRoom;
//        QPostcard qPostcard = QPostcard.postcard;
//        QProfileImageType qProfileImageType = QProfileImageType.profileImageType;
//        QMemberChatRoom qOtherMemberChatRoom = new QMemberChatRoom("memberChatRoom");
//        QMember qOtherMember = QMemberChatRoom.memberChatRoom.member;
//
//        return queryFactory.select(qChatRoom)
//                .from(qPostcard)
//                .innerJoin(qMemberChatRoom).on(qMemberChatRoom.member.eq(qMember))
//                .innerJoin(qChatRoom).on(qMemberChatRoom.chatRoom.eq(qChatRoom))
//                .innerJoin(qPostcard).on(qChatRoom.postcard.eq(qPostcard)).fetchJoin()
//                .innerJoin(qOtherMemberChatRoom).on(qOtherMemberChatRoom.chatRoom.eq(qChatRoom))
//                .innerJoin(qOtherMember).on(qOtherMemberChatRoom.member.eq(qOtherMember)).fetchJoin()
//                .innerJoin(qProfileImageType).on(qOtherMember.memberStyle.profileImageType.eq(qProfileImageType))
//                .where(qOtherMember.id.ne(memberId))
//                .where(qMember.id.eq(memberId))
//                .orderBy(qChatRoom.lastChatTime.desc())
//                .fetch();
//    }

    public List<ChatRoomResponse> findByMemberId(Long memberId) {
        QMember qMember = QMember.member;
        QPostcard qPostcard = QPostcard.postcard;
        QProfileImageType qProfileImageType = QProfileImageType.profileImageType;
        QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
        QChatRoom qChatRoom = QChatRoom.chatRoom;

        return queryFactory.select(Projections.fields(ChatRoomResponse.class,
                qChatRoom.id.as("id"),
                Projections.fields(ChatRoomResponse.PostCardResponse.class,
                        qPostcard.id.as("id"),
                        qPostcard.postcardType.as("type"),
                        qPostcard.imageUrl.as("imageUrl"),
                        qPostcard.message.as("message"),
                        qPostcard.postcardStatus.as("status")).as("postcard"),
                Projections.fields(ChatRoomResponse.MemberResponse.class,
                        qMember.id.as("id"),
                        qMember.memberProfile.name.as("name"),
                        qProfileImageType.imageUrl.as("profileImageUrl"),
                        qProfileImageType.gender.as("profileGender")).as("otherMember")))
                .from(qPostcard)
                .innerJoin(qChatRoom).on(qChatRoom.postcard.eq(qPostcard)).fetchJoin()
                .innerJoin(qMemberChatRoom).on(qMemberChatRoom.chatRoom.eq(qChatRoom)).fetchJoin()
                .innerJoin(qMember).on(qMemberChatRoom.member.eq(qMember)).fetchJoin()
                .innerJoin(qProfileImageType).on(qMember.memberStyle.profileImageType.eq(qProfileImageType)).fetchJoin()
                .where(qPostcard.receiveMember.id.eq(memberId)
                        .or(qPostcard.sendMember.id.eq(memberId)))
                .where(qMember.id.ne(memberId))
                .orderBy(qChatRoom.lastChatTime.desc())
                .fetch();
    }
}
