package com.bookbla.americano.domain.postcard.service;


import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBlockRepository;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import com.bookbla.americano.domain.member.service.MemberBookService;
import com.bookbla.americano.domain.memberask.exception.MemberAskExceptionType;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.MemberReplyRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import com.bookbla.americano.domain.memberask.repository.entity.MemberReply;
import com.bookbla.americano.domain.notification.service.AlarmService;
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
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.repository.QuizReplyRepository;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import com.bookbla.americano.domain.quiz.repository.entity.QuizReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PostcardService {

    private final PostcardRepository postcardRepository;
    private final MemberReplyRepository memberReplyRepository;
    private final MemberAskRepository memberAskRepository;
    private final QuizReplyRepository quizReplyRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final MemberRepository memberRepository;
    private final PostcardTypeRepository postcardTypeRepository;
    private final MemberPostcardRepository memberPostcardRepository;
    private final MemberBookRepository memberBookRepository;
    private final MemberBlockRepository memberBlockRepository;
    private final MemberBookService memberBookService;
    private final AlarmService alarmService;

    public SendPostcardResponse send(Long memberId, SendPostcardRequest request) {
        MemberPostcard memberPostcard = memberPostcardRepository.findMemberPostcardByMemberId(
                memberId)
            .orElseThrow(
                () -> new BaseException(MemberExceptionType.EMPTY_MEMBER_POSTCARD_INFO));
        memberPostcard.validate();

        memberBlockRepository.findByBlockerMemberIdAndBlockedMemberId(request.getReceiveMemberId(), memberId)
                .ifPresent(it -> { throw new BaseException(PostcardExceptionType.BLOCKED); });
        List<Postcard> sentPostcards = postcardRepository.findBySendMemberIdAndReceiveMemberId(
            memberId, request.getReceiveMemberId());
        sentPostcards.forEach(Postcard::validateSendPostcard);

        Member member = memberRepository.getByIdOrThrow(memberId);
        Member targetMember = memberRepository.getByIdOrThrow(request.getReceiveMemberId());

        MemberAsk memberAsk = memberAskRepository.findByMember(member)
            .orElseThrow(() -> new BaseException(MemberAskExceptionType.NOT_REGISTERED_MEMBER));
        MemberReply memberReply = MemberReply.builder()
            .memberAsk(memberAsk)
            .content(request.getMemberReply())
            .build();
        memberReplyRepository.save(memberReply);

        List<QuizReply> correctReplies = new ArrayList<>();
        boolean isCorrect = false;
        for (SendPostcardRequest.QuizAnswer quizAnswer : request.getQuizAnswerList()) {
            QuizQuestion quizQuestion = quizQuestionRepository.getByIdOrThrow(
                quizAnswer.getQuizId());
            CorrectStatus status = quizQuestion.solve(quizAnswer.getQuizAnswer());

            QuizReply quizReply = QuizReply.builder()
                .quizQuestion(quizQuestion)
                .answer(quizAnswer.getQuizAnswer())
                .member(member)
                .correctStatus(status)
                .build();
            correctReplies.add(quizReply);

            if (status == CorrectStatus.CORRECT) {
                isCorrect = true;
            }
        }

        PostcardStatus status = isCorrect ? PostcardStatus.PENDING : PostcardStatus.ALL_WRONG;

        memberPostcard.use();

        PostcardType postCardType = postcardTypeRepository.getByIdOrThrow(
            request.getPostcardTypeId());
        Postcard postcard = Postcard.builder()
            .sendMember(member)
            .receiveMember(targetMember)
            .postcardStatus(status)
            .memberReply(memberReply)
            .postcardType(postCardType)
            .imageUrl(postCardType.getImageUrl())
            .build();
        postcardRepository.save(postcard);

        for (QuizReply quizReply : correctReplies) {
            quizReply.updatePostcard(postcard);
            quizReplyRepository.save(quizReply);
        }

        // 엽서를 받는 멤버에게 푸시 알림
        alarmService.sendPushAlarmForReceivePostCard(member, targetMember);

        return SendPostcardResponse.builder().isSendSuccess(isCorrect).build();
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
            nowResponse = new MemberPostcardFromResponse(i.getMemberId(), i.getMemberName(),
                i.getMemberBirthDate(), i.getMemberGender(), i.getMemberSchoolName(),
                i.getMemberProfileImageUrl(),
                i.getMemberOpenKakaoRoomUrl(), i.getPostcardId(), i.getPostcardStatus());
            for (MemberBookReadResponses.MemberBookReadResponse j : memberBookList.getMemberBookReadResponses()) {
                if (j.isRepresentative()) {
                    nowResponse.setRepresentativeBookTitle(j.getTitle());
                    nowResponse.setRepresentativeBookAuthor(j.getAuthors());
                    nowBookImageUrls.add(0, j.getThumbnail());
                } else {
                    nowBookImageUrls.add(j.getThumbnail());
                }
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
        List<MemberPostcardToResponse> memberPostcardToResponseList = new ArrayList<>();
        long now = -1;
        MemberPostcardToResponse nowResponse = new MemberPostcardToResponse();
        List<String> nowBookTitles = new ArrayList<>();
        List<String> nowBookImageUrls = new ArrayList<>();
        List<CorrectStatus> nowCorrectStatuses = new ArrayList<>();
        int nowScore = 0;
        for (PostcardToResponse i : postcardToResponseList) {
            if (i.getMemberId() != now) {
                if (now != -1) {
                    nowResponse.setBookTitles(nowBookTitles);
                    nowResponse.setBookImageUrls(nowBookImageUrls);
                    nowResponse.setCorrectStatuses(nowCorrectStatuses);
                    nowResponse.setQuizScore(nowScore);
                    // 리스트에 추가
                    memberPostcardToResponseList.add(nowResponse);
                }
                // 초기화
                now = i.getMemberId();
                nowResponse = new MemberPostcardToResponse(i.getPostcardId(), i.getMemberId(),
                    i.getMemberName(), i.getMemberProfileImageUrl(), i.getMemberBirthDate(),
                    i.getMemberGender(),
                    i.getDrinkType(), i.getSmokeType(), i.getContactType(), i.getDateStyleType(),
                    i.getDateCostType(), i.getMbti(), i.getJustFriendType(),
                    i.getMemberSchoolName(), i.getMemberReplyContent(), i.getPostcardStatus(),
                    i.getPostcardImageUrl(), i.getMemberKakaoRoomUrl());
                nowBookTitles = new ArrayList<>();
                nowBookImageUrls = new ArrayList<>();
                nowCorrectStatuses = new ArrayList<>();
                nowScore = 0;

                // 책 퀴즈 정답 여부 저장
                nowBookTitles.add(i.getBookTitle());
                nowBookImageUrls.add(i.getBookImageUrl());
                nowCorrectStatuses.add(i.getCorrectStatus());
                if (i.getCorrectStatus().equals(CorrectStatus.CORRECT)) {
                    nowScore++;
                }
            } else {
                // 책 퀴즈 정답 여부 저장
                nowBookTitles.add(i.getBookTitle());
                nowBookImageUrls.add(i.getBookImageUrl());
                nowCorrectStatuses.add(i.getCorrectStatus());
                if (i.getCorrectStatus().equals(CorrectStatus.CORRECT)) {
                    nowScore++;
                }
            }
        }
        if (!nowBookTitles.isEmpty()) {
            nowResponse.setBookTitles(nowBookTitles);
            nowResponse.setBookImageUrls(nowBookImageUrls);
            nowResponse.setCorrectStatuses(nowCorrectStatuses);
            nowResponse.setQuizScore(nowScore);

            // 리스트에 추가
            memberPostcardToResponseList.add(nowResponse);
        }
        return memberPostcardToResponseList;
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
        MemberPostcard memberPostcard = memberPostcardRepository.findMemberPostcardByMemberId(
                memberId)
            .orElseThrow(
                () -> new BaseException(MemberExceptionType.EMPTY_MEMBER_POSTCARD_INFO));
        memberPostcard.use();
        updatePostcardStatus(memberId, postcardId, PostcardStatus.READ);
    }

    public PostcardStatus getPostcardStatus(Long postcardId) {
        Postcard postcard = postcardRepository.findById(postcardId)
            .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));

        return postcard.getPostcardStatus();
    }

    public void updatePostcardStatus(Long memberId, Long postcardId,
                                     PostcardStatus postcardStatus) {
        Postcard postcard = postcardRepository.findById(postcardId)
            .orElseThrow(() -> new BaseException(PostcardExceptionType.INVALID_POSTCARD));
        Member member = memberRepository.getByIdOrThrow(memberId);
        if (!member.equals(postcard.getSendMember()) && !member.equals(
            postcard.getReceiveMember())) {
            throw new BaseException(PostcardExceptionType.ACCESS_DENIED_TO_POSTCARD);
        }

        postcardRepository.updatePostcardStatus(postcardStatus, postcardId);

        // 상태가 수락으로 변경되면 엽서를 보낸 멤버에게 푸시 알림
        if (postcardStatus.isAccept()) {
            Member sendMember = postcard.getSendMember();
            Member receiveMember = postcard.getReceiveMember();
            alarmService.sendPushAlarmForAcceptPostcard(sendMember, receiveMember);
        }
    }

    @Transactional(readOnly = true)
    public PostcardSendValidateResponse validateSendPostcard(Long sendMemberId, Long targetMemberId) {
        memberBlockRepository.findByBlockerMemberIdAndBlockedMemberId(targetMemberId, sendMemberId)
                .ifPresent(it -> { throw new BaseException(PostcardExceptionType.BLOCKED); });
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
