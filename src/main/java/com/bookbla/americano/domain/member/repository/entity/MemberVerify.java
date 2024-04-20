package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
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

import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.FAIL;
import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.SUCCESS;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberVerify extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    // 이미지 혹은 카카오톡방 링크
    private String value;

    private String description;

    @Enumerated(EnumType.STRING)
    private MemberVerifyType type;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberVerifyStatus status = PENDING;

    public void success() {
        this.status = SUCCESS;
        this.description = "성공";
    }

    public void fail(String failReason) {
        this.status = FAIL;
        this.description = failReason;
    }
}
