package com.bookbla.americano.domain.memberask.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberReplyUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberReplyResponse;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import com.bookbla.americano.domain.memberask.exception.MemberReplyExceptionType;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberReplyService {

    private final PostcardRepository postcardRepository;
    private final MemberAskRepository memberAskRepository;

    public MemberReplyResponse readMemberReply(Long memberId, Long postcardId) {
        Postcard postcard = postcardRepository.findById(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
        if (!Objects.equals(postcard.getSendMember().getId(), memberId)) {
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        }

        MemberAsk memberAsk = memberAskRepository.findByMember(postcard.getReceiveMember())
                .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));

        return new MemberReplyResponse(postcard.getMemberReply().getId(),
                memberAsk.getContents(), postcard.getMemberReply().getContent());
    }

    public void updateMemberReply(Long memberId, MemberReplyUpdateRequest memberReplyUpdateRequest) {
        Postcard postcard = postcardRepository.findById(memberReplyUpdateRequest.getPostcardId())
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
        if (!Objects.equals(postcard.getSendMember().getId(), memberId)) {
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        } else if (!postcard.getPostcardStatus().equals(PostcardStatus.PENDING)) {
            throw new BaseException(MemberReplyExceptionType.IMMUTABLE_POSTCARD);
        }
        postcard.getMemberReply().updateContent(memberReplyUpdateRequest.getContent());
    }
}
