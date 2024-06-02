package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.request.MemberContactCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactReadResponse;
import com.bookbla.americano.domain.member.repository.MemberContactRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberContact;
import com.bookbla.americano.domain.member.service.MemberContactService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberContactServiceImpl implements MemberContactService {

    private final MemberRepository memberRepository;
    private final MemberContactRepository memberContactRepository;

    @Override
    @Transactional
    public MemberContactCreateResponse createMemberContacts(Long memberId,
        MemberContactCreateRequest memberContactCreateRequest) {

        Member member = memberRepository.getByIdOrThrow(memberId);
        List<String> phoneNumbers = memberContactCreateRequest.getPhoneNumbers();

        for (String phoneNumber: phoneNumbers) {
            MemberContact memberContact = MemberContact.builder()
                .member(member)
                .otherPhoneNumber(phoneNumber)
                .build();

            memberContactRepository.save(memberContact);
        }

        return MemberContactCreateResponse.from(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberContactReadResponse readMemberContacts(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberContact> memberContacts = memberContactRepository.findByMember(member);

        List<String> phoneNumbers = memberContacts.stream()
            .map(MemberContact::getOtherPhoneNumber)
            .collect(Collectors.toList());

        return MemberContactReadResponse.from(member, phoneNumbers);
    }

    @Transactional(readOnly = true)
    public List<Member> avoidKnowMembers(Long memberId) {
        return memberRepository.findMembersWithPhoneNumbersNotInContacts(memberId);
    }

}
