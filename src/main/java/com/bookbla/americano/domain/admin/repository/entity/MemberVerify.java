package com.bookbla.americano.domain.admin.repository.entity;

import java.time.LocalDateTime;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class MemberVerify extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String value;

    private String failReason;

    @Enumerated(EnumType.STRING)
    private VerifyStatus status;

    @Enumerated(EnumType.STRING)
    private VerifyType type;

    @CreatedDate
    private LocalDateTime createdAt;

    enum VerifyType {
        OPEN_KAKAO_ROOM_URL, PROFILE_IMAGE, STUDENT_ID
    }

    enum VerifyStatus {
        SUCCESS, FAIL
    }
}

