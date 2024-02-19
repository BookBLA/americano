package com.bookbla.americano.domain.memberask.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberAskCreateRequest {

    // 등록하지 않을 수도 있나?
    @NotNull(message = "개인 질문이 입력되지 않았습니다.")
    private String contents;

    public MemberAsk toMemberAskWith(Member member) {
        return MemberAsk.builder()
                .member(member)
                .contents(contents)
                .build();
    }
}
