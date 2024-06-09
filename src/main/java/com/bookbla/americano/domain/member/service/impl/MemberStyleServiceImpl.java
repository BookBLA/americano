package com.bookbla.americano.domain.member.service.impl;

import static com.bookbla.americano.domain.member.enums.MemberStatus.BOOK;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberStatusLog;
import com.bookbla.americano.domain.member.repository.entity.MemberStyle;
import com.bookbla.americano.domain.member.service.MemberStyleService;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberStyleServiceImpl implements MemberStyleService {

    private final MemberRepository memberRepository;
    private final MemberStatusLogRepository memberStatusLogRepository;
    private final MemberAskRepository memberAskRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberStyleResponse readMemberStyle(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = member.getMemberStyle();

        MemberAsk memberAsk = memberAskRepository.findByMember(member)
            .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));

        return MemberStyleResponse.of(member, memberStyle, memberAsk);
    }

    @Override
    public MemberStyleResponse createMemberStyle(Long memberId,
                                                 MemberStyleCreateRequest memberStyleCreateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.validateStyleRegistered();
        member.updateMemberStyle(memberStyleCreateRequest.toMemberStyle());

        MemberAsk memberAsk = MemberAsk.builder()
            .member(member)
            .contents(memberStyleCreateRequest.getMemberAsk())
            .build();
        MemberAsk savedMemberAsk = memberAskRepository.save(memberAsk);

        MemberStatus beforeStatus = member.getMemberStatus();
        member.updateMemberStatus(BOOK, LocalDateTime.now());
        memberRepository.save(member);

        memberStatusLogRepository.save(
            MemberStatusLog.builder()
                .memberId(member.getId())
                .beforeStatus(beforeStatus)
                .afterStatus(BOOK)
                .build()
        );
        return MemberStyleResponse.of(member, member.getMemberStyle(), savedMemberAsk);
    }

    @Override
    public void updateMemberStyle(Long memberId,
                                  MemberStyleUpdateRequest memberStyleUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberStyle memberStyle = member.getMemberStyle();

        MemberAsk memberAsk = memberAskRepository.findByMember(member)
            .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));

        memberAsk.updateContent(memberStyleUpdateRequest.getMemberAsk());
        updateMemberStyle(memberStyle, memberStyleUpdateRequest);
    }

    private void updateMemberStyle(MemberStyle memberStyle, MemberStyleUpdateRequest request) {
        memberStyle.updateMbti(request.getMbti())
            .updateDrinkType(request.getDrinkType())
            .updateDateCostType(request.getDateCostType())
            .updateSmokeType(request.getSmokeType())
            .updateContactType(request.getContactType())
            .updateDateStyleType(request.getDateStyleType())
            .updateJustFriendType(request.getJustFriendType());
    }
}
