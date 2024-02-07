package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.MemberStyle;
import com.bookbla.americano.domain.member.controller.dto.StylesResponse;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyle;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStyleRepository;
import com.bookbla.americano.domain.member.service.dto.MemberStyleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberStyleService {

    private final MemberRepository memberRepository;
    private final MemberStyleRepository memberStyleRepository;

    public StylesResponse readStyles() {
        return StylesResponse.of(
                SmokeType.getValues(),
                DrinkType.getValues(),
                ContactType.getValues(),
                DateStyle.getValues(),
                DateCostType.getValues(),
                JustFriendType.getValues()
        );
    }

    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = memberStyleRepository.getByMemberOrThrow(member);

        return MemberStyleResponse.of(member, memberStyle);
    }

}
