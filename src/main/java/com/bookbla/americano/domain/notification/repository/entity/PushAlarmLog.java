package com.bookbla.americano.domain.notification.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.notification.enums.PushAlarmStatus;
import com.bookbla.americano.domain.notification.enums.PushAlarmType;

import javax.persistence.*;

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
@Table(indexes = @Index(name = "FK_Member_PushAlarmLog", columnList = "memberId"))
public class PushAlarmLog extends BaseEntity {

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
