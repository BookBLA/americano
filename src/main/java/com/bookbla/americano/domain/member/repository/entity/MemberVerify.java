package com.bookbla.americano.domain.member.repository.entity;

import java.time.LocalDateTime;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class MemberVerify extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String value;

    private String failReason;

    @Enumerated(EnumType.STRING)
    private MemberVerifyStatus memberVerifyStatus;

    @Enumerated(EnumType.STRING)
    private MemberVerifyType memberVerifyType;

    @CreatedDate
    private LocalDateTime createdAt;

    public void updateStatus(MemberVerifyStatus status) {
        if (status == MemberVerifyStatus.PENDING) {
            throw new IllegalStateException("대기상태로는 바꿀 수 없어용");
        }
        this.memberVerifyStatus = status;
    }

}
