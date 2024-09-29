package com.bookbla.americano.domain.postcard.service;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.repository.MatchExcludedRepository;
import com.bookbla.americano.domain.matching.repository.MatchIgnoredRepository;
import com.bookbla.americano.domain.matching.repository.entity.MatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MatchIgnoredInfo;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.*;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.service.MemberBookService;
import com.bookbla.americano.domain.notification.event.PostcardAlarmEvent;
import com.bookbla.americano.domain.notification.event.PushAlarmEventHandler;
import com.bookbla.americano.domain.postcard.controller.dto.response.ContactInfoResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardFromResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardToResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.PostcardTypeRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardTypeResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostcardService {

    private final PostcardRepository postcardRepository;
    private final MemberRepository memberRepository;
    private final PostcardTypeRepository postcardTypeRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberBlockRepository memberBlockRepository;
    private final MemberBookService memberBookService;
    private final PushAlarmEventHandler postcardPushAlarmEventListener;
    private final MatchExcludedRepository matchExcludedRepository;
    private final MatchIgnoredRepository matchIgnoredRepository;

    public SendPostcardResponse send(Long memberId, SendPostcardRequest request) {
        // 엽서 보내는 회원의 학생증 상태 검증
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.validateStudentIdStatusRegistered();

        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(
                        memberId)
                .orElseThrow(
                        () -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));
        memberBookmark.validateSendPostcard();

        memberBlockRepository.findByBlockerMemberIdAndBlockedMemberId(request.getReceiveMemberId(), memberId)
                .ifPresent(it -> {
                    throw new BaseException(PostcardExceptionType.BLOCKED);
                });
        List<Postcard> sentPostcards = postcardRepository.findBySendMemberIdAndReceiveMemberId(
                memberId, request.getReceiveMemberId());
        sentPostcards.forEach(Postcard::validateSendPostcard);
        PostcardStatus status = PostcardStatus.PENDING;

        Member targetMember = memberRepository.getByIdOrThrow(request.getReceiveMemberId());
        MemberBook targetMemberBook = memberBookRepository.getByIdOrThrow(request.getReceiveMemberId());
        targetMemberBook.validateOwner(targetMember);
        memberBookmark.sendPostcard();

        updateMemberMatchingExcluded(member, targetMember);

        PostcardType postCardType = postcardTypeRepository.getByIdOrThrow(
                request.getPostcardTypeId());
        Postcard postcard = Postcard.builder()
                .sendMember(member)
                .receiveMember(targetMember)
                .postcardStatus(status)
                .message(request.getMemberReply())
                .postcardType(postCardType)
                .imageUrl(postCardType.getImageUrl())
                .build();
        postcardRepository.save(postcard);


        postcardPushAlarmEventListener.sendPostcard(new PostcardAlarmEvent(member, targetMember));

        return SendPostcardResponse.builder().isSendSuccess(true).build();
    }

    public PostcardTypeResponse getPostcardTypeList() {
        List<PostcardType> postcardTypeList = postcardTypeRepository.findAll();
        return PostcardTypeResponse.of(postcardTypeList);
    }

    // 보낸 엽서
    public List<MemberPostcardFromResponse> getPostcardsFromMember(Long memberId) {
        List<PostcardFromResponse> postcardFromResponseList = postcardRepository.getPostcardsFromMember(
                memberId);
        List<MemberPostcardFromResponse> memberPostcardFromResponseList = new ArrayList<>();

        for (PostcardFromResponse i : postcardFromResponseList) {
            MemberPostcardFromResponse nowResponse;
            MemberBookReadResponses memberBookList = memberBookService.readMemberBooks(
                    i.getMemberId());
            List<String> nowBookImageUrls = new ArrayList<>();
            nowResponse = new MemberPostcardFromResponse(i);
            for (MemberBookReadResponses.MemberBookReadResponse j : memberBookList.getMemberBookReadResponses()) {
                nowResponse.setRepresentativeBookTitle(j.getTitle());
                nowResponse.setRepresentativeBookAuthor(j.getAuthors());
                nowBookImageUrls.add(0, j.getThumbnail());
            }
            nowResponse.setBookImageUrls(nowBookImageUrls);
            memberPostcardFromResponseList.add(nowResponse);
        }

        return memberPostcardFromResponseList;
    }

    // 받은 엽서
    public List<MemberPostcardToResponse> getPostcardsToMember(Long memberId) {
        List<PostcardToResponse> postcardToResponseList = postcardRepository.getPostcardsToMember(
                memberId);

        return postcardToResponseList.stream()
                .map(MemberPostcardToResponse::new)
                .collect(Collectors.toList());
    }

    public void readMemberPostcard(Long memberId, Long postcardId) {
        Postcard postcard = postcardRepository.findById(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
        if (!Objects.equals(postcard.getReceiveMember().getId(), memberId)) {
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        } else if (!postcard.getPostcardStatus().equals(PostcardStatus.PENDING)) {
            if (postcard.getPostcardStatus() == PostcardStatus.ALL_WRONG) {
                throw new BaseException(PostcardExceptionType.ALL_WRONG_POSTCARD);
            } else {
                throw new BaseException(PostcardExceptionType.READ_POSTCARD_ALREADY);
            }
        }
        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(
                        memberId)
                .orElseThrow(
                        () -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));
        memberBookmark.readPostcard();
        updatePostcardStatus(memberId, postcardId, PostcardStatus.READ, null);
    }

    public PostcardStatus getPostcardStatus(Long postcardId) {
        Postcard postcard = postcardRepository.findById(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));

        return postcard.getPostcardStatus();
    }

    public void updatePostcardStatus(Long memberId, Long postcardId,
                                     PostcardStatus postcardStatus, Long memberBookId) {
        Postcard postcard = postcardRepository.findById(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
        Member member = memberRepository.getByIdOrThrow(memberId);
        if (!member.equals(postcard.getSendMember()) && !member.equals(
                postcard.getReceiveMember())) {
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        }

        postcardRepository.updatePostcardStatus(postcardStatus, postcardId);

        // 상태가 수락으로 변경되면 엽서를 보낸 멤버에게 푸시 알림
        Member sendMember = postcard.getSendMember();
        Member receiveMember = postcard.getReceiveMember();

        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(
                        postcard.getSendMember().getId())
                .orElseThrow(
                        () -> new BaseException(MemberExceptionType.EMPTY_MEMBER_BOOKMARK_INFO));

        if (postcardStatus.isAccept()) {
            updateMemberMatchingExcluded(sendMember, receiveMember);
            memberBookmark.acceptPostcard();
            postcardPushAlarmEventListener.acceptPostcard(new PostcardAlarmEvent(sendMember, receiveMember));

        } else if (postcardStatus.isPending()) {
            updateMemberMatchingExcluded(sendMember, receiveMember);

        } else if (postcardStatus.isRefused()) { // 거절 시 환불
            updateMemberMatchingIgnored(sendMember, receiveMember, memberBookId);

            postcard.updatePostcardStatusRefusedAt();
            memberBookmark.addBookmark(35);
        }
    }

    private void updateMemberMatchingExcluded(Member sendMember, Member receiveMember) {
        matchExcludedRepository.findByMemberIdAndExcludedMemberId(sendMember.getId(), receiveMember.getId())
                        .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(sendMember.getId(), receiveMember.getId())));

        matchExcludedRepository.findByMemberIdAndExcludedMemberId(receiveMember.getId(), sendMember.getId())
                        .orElseGet(() -> matchExcludedRepository.save(MatchExcludedInfo.of(receiveMember.getId(), sendMember.getId())));
    }

    private void updateMemberMatchingIgnored(Member sendMember, Member receiveMember, Long memberBookId) {
        matchIgnoredRepository.findByMemberIdAndIgnoredMemberIdAndIgnoredMemberBookId(
                sendMember.getId(), receiveMember.getId(), memberBookId)
                .orElseGet(() -> matchIgnoredRepository.save(MatchIgnoredInfo.from(sendMember.getId(), receiveMember.getId(), memberBookId)));
    }

    @Transactional(readOnly = true)
    public PostcardSendValidateResponse validateSendPostcard(Long sendMemberId, Long targetMemberId) {
        memberBlockRepository.findByBlockerMemberIdAndBlockedMemberId(targetMemberId, sendMemberId)
                .ifPresent(it -> {
                    throw new BaseException(PostcardExceptionType.BLOCKED);
                });
        List<Postcard> sendPostcards = postcardRepository.findBySendMemberIdAndReceiveMemberId(
                sendMemberId, targetMemberId);
        sendPostcards.forEach(Postcard::validateSendPostcard);

        boolean isRefused = sendPostcards.stream().anyMatch(Postcard::isRefused);

        return PostcardSendValidateResponse.from(isRefused);
    }

    public ContactInfoResponse getContactInfo(Long memberId, Long postcardId) {
        Postcard postcard = postcardRepository.findById(postcardId)
                .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));

        if (!Objects.equals(postcard.getReceiveMember().getId(), memberId) && !Objects.equals(postcard.getSendMember().getId(), memberId))
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        if (postcard.getPostcardStatus() != PostcardStatus.ACCEPT)
            throw new BaseException(PostcardExceptionType.POSTCARD_NOT_ACCEPTED);

        if (Objects.equals(postcard.getReceiveMember().getId(), memberId)) {
            Member targetMember = memberRepository.getByIdOrThrow(postcard.getSendMember().getId());
            List<MemberBook> targetMemberBooks = memberBookRepository.findByMemberOrderByCreatedAt(targetMember);
            return new ContactInfoResponse(targetMember, targetMemberBooks);
        } else if (Objects.equals(postcard.getSendMember().getId(), memberId)) {
            Member targetMember = memberRepository.getByIdOrThrow(postcard.getReceiveMember().getId());
            List<MemberBook> targetMemberBooks = memberBookRepository.findByMemberOrderByCreatedAt(targetMember);
            return new ContactInfoResponse(targetMember, targetMemberBooks);
        }

        throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
    }
}
