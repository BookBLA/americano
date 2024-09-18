package com.bookbla.americano.domain.notification.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.notification.controller.dto.request.InAppAlarmRequest;
import com.bookbla.americano.domain.notification.enums.InAppAlarmStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InAppAlarmLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String imageUrl;

    private String title;

    private String body;

    private InAppAlarmStatus status;

    public void setStatus(InAppAlarmStatus status) {
        this.status = status;
    }

    public static InAppAlarmLog from(InAppAlarmRequest req, Member member) {
        return InAppAlarmLog.builder()
                .body(req.getBody())
                .title(req.getTitle())
                .imageUrl(req.getImageUrl())
                .member(member)
                .build();
    }

}
