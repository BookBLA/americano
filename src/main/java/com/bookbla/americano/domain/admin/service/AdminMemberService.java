package com.bookbla.americano.domain.admin.service;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberProfileStatusResponse;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberReadResponses;
import com.bookbla.americano.domain.admin.controller.dto.response.AdminMemberStudentIdResponses;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberVerify;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.member.enums.MemberVerifyStatus.PENDING;
import static com.bookbla.americano.domain.member.enums.MemberVerifyType.STUDENT_ID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminMemberService {

    private final MemberRepository memberRepository;
    private final MemberVerifyRepository memberVerifyRepository;

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readMembers(Pageable pageable) {
        long count = memberRepository.count();
        Page<Member> memberPaging = memberRepository.findAll(pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(count, members);
    }

    public AdminMemberProfileStatusResponse readProfileStatuses() {
        return AdminMemberProfileStatusResponse.of(
                StudentIdImageStatus.getValues(),
                MemberVerifyStatus.getValues()
        );
    }

    @Transactional(readOnly = true)
    public AdminMemberReadResponses readDeletedMembers(Pageable pageable) {
        long count = memberRepository.countByMemberStatus(MemberStatus.DELETED);
        Page<Member> memberPaging = memberRepository.findByMemberStatus(MemberStatus.DELETED, pageable);
        List<Member> members = memberPaging.getContent();
        return AdminMemberReadResponses.from(count, members);
    }

    @Transactional(readOnly = true)
    public AdminMemberStudentIdResponses readStudentIdImagePendingMembers(Pageable pageable) {
        long count = memberVerifyRepository.countByVerifyTypeAndVerifyStatus(STUDENT_ID, PENDING);
        Page<MemberVerify> paging = memberVerifyRepository.findByVerifyTypeAndVerifyStatus(STUDENT_ID, PENDING, pageable);
        List<MemberVerify> memberVerifies = paging.getContent();
        return AdminMemberStudentIdResponses.from(count, memberVerifies);
    }
}
