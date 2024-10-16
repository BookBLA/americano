package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import java.util.Objects;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "FK_Member_PushAlarm", columnList = "member_id"))
public class MemberPushAlarm extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;

    private String body;

    public void validateOwner(Long memberId) {
        if (!Objects.equals(member.getId(), memberId)) {
            throw new BaseException(PushAlarmExceptionType.INVALID_OWNER);
        }
    }

    public static MemberPushAlarm fromPushAlarmForm(Member member, PushAlarmForm pushAlarmForm) {
        return MemberPushAlarm.builder()
                .member(member)
                .title(pushAlarmForm.getTitle())
                .body(pushAlarmForm.getBody())
                .build();
    }

}
