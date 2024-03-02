package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
	@Override
    @Transactional(readOnly = true)
    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return MemberResponse.from(member);
    }
    @Override
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        update(member, memberUpdateRequest);

        return MemberResponse.from(member);
    }

    private void update(Member member, MemberUpdateRequest request) {
        member.updateOauthEmail(request.getOauthEmail())
            .updateMemberType(request.getMemberType());
   	}
   	
    @Override
    public Member getMemberById(Long memberId) {
        return memberRepository.getByIdOrThrow(memberId);
    }
}
