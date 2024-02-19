package com.bookbla.americano.domain.memberask.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAsk extends BaseInsertEntity {

    private static final int MAX_CONTENTS_LENGTH = 80;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String contents;

    public MemberAsk(Member member, String contents) {
        this(null, member, contents);
    }

    public MemberAsk(Long id, Member member, String contents) {
        validate(contents);
        this.id = id;
        this.member = member;
        this.contents = contents;
    }

    private void validate(String contents) {
        if (contents.length() > MAX_CONTENTS_LENGTH) {
            throw new BaseException(MemberAskExceptionType.INVALID_CONTENT_LENGTH);
        }
    }

    public void updateContent(String contents) {
        validate(contents);
        this.contents = contents;
    }
}
