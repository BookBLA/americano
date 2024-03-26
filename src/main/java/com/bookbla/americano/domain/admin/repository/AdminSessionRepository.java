package com.bookbla.americano.domain.admin.repository;

import java.util.Optional;

import com.bookbla.americano.domain.admin.repository.entity.AdminSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSessionRepository extends JpaRepository<AdminSession, Long> {

    Optional<AdminSession> findBySessionId(String sessionId);
}
