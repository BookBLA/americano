package com.bookbla.americano.domain.chat.repository.custom.impl;

import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;
import com.bookbla.americano.domain.chat.repository.custom.ChatRoomRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.QChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QMemberChatRoom;
import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.member.repository.entity.QProfileImageType;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.school.repository.entity.QSchool;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatRoomResponse> findByMemberId(Long memberId) {
        QMember qMemberReq = new QMember("memberReq");
        QMember qMemberOther = new QMember("otherMember");
        QMemberChatRoom qMemberChatRoomReq = new QMemberChatRoom("memberChatRoomReq");
        QMemberChatRoom qMemberChatRoomOther = new QMemberChatRoom("memberChatRoomOther");
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QPostcard qPostcard = QPostcard.postcard;
        QProfileImageType qProfileImageType = QProfileImageType.profileImageType;
        QSchool qSchool = QSchool.school;
        return queryFactory.select(Projections.fields(ChatRoomResponse.class,
                qChatRoom.id.as("id"),
                qMemberChatRoomOther.unreadCount.as("unreadCount"),
                qChatRoom.lastChat.as("lastChat"),
                qChatRoom.lastChatTime.as("lastChatTime"),
                qMemberChatRoomReq.isAlert.as("isAlert"),
                Projections.fields(ChatRoomResponse.PostCardResponse.class,
                        qPostcard.id.as("postcardId"),
                        qPostcard.postcardType.as("type"),
                        qPostcard.imageUrl.as("imageUrl"),
                        qPostcard.message.as("message"),
                        qPostcard.postcardStatus.as("status")).as("postcard"),
                Projections.fields(ChatRoomResponse.MemberResponse.class,
                        qMemberOther.id.as("memberId"),
                        qMemberOther.memberProfile.name.as("name"),
                        qSchool.name.as("schoolName"),
                        qMemberOther.memberStyle.height.as("height"),
                        qMemberOther.memberStyle.smokeType.as("smokeType"),
                        qMemberOther.memberStyle.mbti.as("mbti"),
                        qProfileImageType.imageUrl.as("profileImageUrl"),
                        qProfileImageType.gender.as("profileGender")).as("otherMember")))
                .from(qMemberReq)
                .innerJoin(qMemberChatRoomReq).on(qMemberChatRoomReq.member.eq(qMemberReq))
                .innerJoin(qChatRoom).on(qMemberChatRoomReq.chatRoom.eq(qChatRoom))
                .innerJoin(qPostcard).on(qChatRoom.postcard.eq(qPostcard))
                .innerJoin(qMemberChatRoomOther).on(qMemberChatRoomOther.chatRoom.eq(qChatRoom))
                .innerJoin(qMemberOther).on(qMemberChatRoomOther.member.eq(qMemberOther))
                .leftJoin(qProfileImageType).on(qMemberOther.memberStyle.profileImageType.eq(qProfileImageType))
                .innerJoin(qSchool).on(qMemberOther.school.eq(qSchool))
                .where(qMemberReq.id.eq(memberId))
                .where(qMemberOther.id.ne(memberId))
                .fetch();
    }
}
