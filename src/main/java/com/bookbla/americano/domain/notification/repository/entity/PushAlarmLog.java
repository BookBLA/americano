package com.bookbla.americano.domain.notification.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.notification.enums.PushAlarmStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmType;
import javax.persistence.Entity;
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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushAlarmLog extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private PushAlarmType pushAlarmType;

    @Enumerated(EnumType.STRING)
    private PushAlarmStatus pushAlarmStatus;

    private String title;
    private String body;
}
