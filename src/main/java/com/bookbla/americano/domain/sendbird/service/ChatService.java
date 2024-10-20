package com.bookbla.americano.domain.sendbird.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.notification.event.PostcardAlarmEvent;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.sendbird.controller.dto.request.EntryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final MemberBookmarkRepository memberBookmarkRepository;
    private final PostcardRepository postcardRepository;
    private final PushAlarmEventHandler postcardPushAlarmEventListener;
    private final MatchExcludedRepository matchExcludedRepository;

    public void updatePostcardStatusByChat(EntryRequest entryRequest, PostcardStatus postcardStatus, Long postcardId) {
        Postcard postcard = postcardRepository.findByIdWithMembers(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));

        postcardRepository.updatePostcardStatus(postcardStatus, postcard.getId());

        Member sendMember = postcard.getSendMember();
        Member receiveMember = postcard.getReceiveMember();

        String sendMemberName = sendMember.getMemberProfile().getName();

        if (postcardStatus.isAccept()) {
            updateBookmarkOnChatAccept(entryRequest.getTargetMemberId());
            postcardPushAlarmEventListener.acceptPostcard(new PostcardAlarmEvent(sendMemberName, receiveMember));
        } else if (postcardStatus.isRefused()) {
            Postcard updatePostcard = postcardRepository.findById(postcardId)
                    .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
            postcardRepository.save(updatePostcard.updatePostcardStatusRefusedAt());
            updateBookmarkOnChatReject(entryRequest.getTargetMemberId());
            deleteMemberMatchingExcluded(sendMember, receiveMember);
        }
    }

    public void updateBookmarkOnChatAccept(Long targetMemberId) {
        MemberBookmark targetMemberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(targetMemberId) // 채팅 수락한 사람 책갈피
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        targetMemberBookmark.acceptChat();
    }

    public void updateBookmarkOnChatReject(Long targetMemberId) {
        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(targetMemberId) // 채팅 보낸 사람 책갈피
                .orElseThrow(() -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        memberBookmark.rejectChat();
    }

    private void deleteMemberMatchingExcluded(Member sendMember, Member receiveMember) {
        matchExcludedRepository.deleteByMemberIdAndExcludedMemberId(sendMember.getId(), receiveMember.getId());
        matchExcludedRepository.deleteByMemberIdAndExcludedMemberId(receiveMember.getId(), sendMember.getId());
    }
}