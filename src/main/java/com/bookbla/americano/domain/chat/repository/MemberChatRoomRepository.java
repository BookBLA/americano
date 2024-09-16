package com.bookbla.americano.domain.chat.repository;

import com.bookbla.americano.domain.chat.repository.custom.MemberChatRoomRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long>, MemberChatRoomRepositoryCustom {
    Optional<MemberChatRoom> findByMember_IdAndChatRoom_Id(Long memberId, Long chatRoomId);



}
