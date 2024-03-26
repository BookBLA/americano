package com.bookbla.americano.domain.admin.repository.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.excpetion.AdminExceptionType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class AdminSession {

    private static final int SESSION_SECONDS = 3600; // 30분

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId = UUID.randomUUID().toString();

    private LocalDateTime expiredTime = LocalDateTime.now().plusSeconds(SESSION_SECONDS);

    public void validateExpired() {
        if (expiredTime.isBefore(LocalDateTime.now())) {
            throw new BaseException(AdminExceptionType.SESSION_OVER);
        }
    }
}
