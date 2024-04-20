package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MemberAuthVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberAuthVerifyType;
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

import static com.bookbla.americano.domain.member.enums.MemberAuthVerifyStatus.FAIL;
import static com.bookbla.americano.domain.member.enums.MemberAuthVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberAuthVerifyStatus.SUCCESS;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthVerifyRequest extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    // 이미지 혹은 카카오톡방 링크
    private String value;

    @Builder.Default
    private String failReason = "대기 중";

    @Enumerated(EnumType.STRING)
    private MemberAuthVerifyType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberAuthVerifyStatus status = PENDING;

    public void success() {
        this.status = SUCCESS;
        this.failReason = "성공한 요청입니다.";
    }

    public void fail(String failReason) {
        this.status = FAIL;
        this.failReason = failReason;
    }
}
