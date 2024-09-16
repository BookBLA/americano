package com.bookbla.americano.domain.chat.repository;

import com.bookbla.americano.domain.chat.repository.custom.ChatRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
    Page<Chat> findByChatRoom_Id(Long id, Pageable pageable);
}
