package com.bookbla.americano.domain.admin.service;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberAuthResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.service.dto.StatusUpdateDto;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberStatus.APPROVAL;


@RequiredArgsConstructor
@Transactional
@Service
public class AdminMemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readMembers(Pageable pageable) {
        Page<Member> memberPaging = memberRepository.findAll(pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(members);
    }

    @Transactional(readOnly = true)
    public AdminMemberAuthResponses readApprovalStatusMembers(Pageable pageable) {
        Page<Member> memberPaging = memberRepository.findByMemberStatus(APPROVAL, pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberAuthResponses.from(members);
    }

    public void updateMemberProfileKakaoRoomStatus(StatusUpdateDto statusUpdateDto) {
        OpenKakaoRoomStatus openKakaoRoomStatus = OpenKakaoRoomStatus.from(statusUpdateDto.getStatus());

        Member member = memberRepository.getByIdOrThrow(statusUpdateDto.getMemberId());
        MemberProfile memberProfile = member.getMemberProfile();

        memberProfile.updateOpenKakaoRoomStatus(openKakaoRoomStatus);
        // FCM 붙인 이후엔 성공 푸시알림
    }
}
