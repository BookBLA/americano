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

    public static final String DESCRIPTION_PARSING_FAIL = "승인 도중 파싱 실패. 확인 필요";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    // 이미지 혹은 카카오톡방 링크
    private String contents;

    // 부가정보,
    // 학생증 정보 저장시,
    // "name: 이길여, gender: female, schoolName: 서울대학교, birthDate: 19320612"
    // 와같이 Map을 String으로 파싱해서 넣은 형태
    private String description;

    @Enumerated(EnumType.STRING)
    private MemberVerifyType verifyType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberVerifyStatus verifyStatus = PENDING;

    public void success() {
        this.verifyStatus = SUCCESS;
        this.description = "성공";
    }

    public void fail(String failReason) {
        this.verifyStatus = FAIL;
        this.description = failReason;
    }
}
