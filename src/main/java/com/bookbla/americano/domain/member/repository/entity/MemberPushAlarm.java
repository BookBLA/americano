package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.notification.enums.PushAlarmForm;
import com.bookbla.americano.domain.notification.exception.PushAlarmExceptionType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
        if (member.getId() != memberId) {
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
