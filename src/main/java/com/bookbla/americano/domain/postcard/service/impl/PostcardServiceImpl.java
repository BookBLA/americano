package com.bookbla.americano.domain.postcard.service.impl;


import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.MemberReplyRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import com.bookbla.americano.domain.memberask.repository.entity.MemberReply;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.PostcardTypeRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import com.bookbla.americano.domain.postcard.service.PostcardService;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
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
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostcardServiceImpl implements PostcardService {

    private final PostcardRepository postcardRepository;
    private final MemberReplyRepository memberReplyRepository;
    private final MemberAskRepository memberAskRepository;
    private final QuizReplyRepository quizReplyRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final MemberRepository memberRepository;
    private final PostcardTypeRepository postcardTypeRepository;
    private final MemberPostcardRepository memberPostcardRepository;

    @Override
    public SendPostcardResponse send(Long memberId, SendPostcardRequest sendPostcardRequest) {
        MemberAsk memberAsk = memberAskRepository.findById(sendPostcardRequest.getMemberAskId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 memberAskId 입니다."));

        MemberReply memberReply = MemberReply.builder()
                .memberAsk(memberAsk)
                .content(sendPostcardRequest.getMemberReply())
                .build();
        memberReplyRepository.save(memberReply);

        List<QuizReply> correctReplies = new ArrayList<>();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        MemberPostcard memberPostcard = memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("MemberPostcard not found"));

        if (memberPostcard.getFreePostcardCount() + memberPostcard.getPayPostcardCount()  == 0) {
            return SendPostcardResponse.builder().isSendSuccess(false).build();
        }

        Member targetMember = memberRepository.findById(memberAsk.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("Target member not found"));

        boolean isCorrect = false;
        for (SendPostcardRequest.QuizAnswer quizAnswer : sendPostcardRequest.getQuizAnswerList()) {
            QuizQuestion quizQuestion = quizQuestionRepository.findById(quizAnswer.getQuizId())
                    .orElseThrow(() -> new IllegalArgumentException("QuizQuestion not found"));

            CorrectStatus status = quizQuestion.getFirstChoice().equals(quizAnswer.getQuizAnswer()) ? CorrectStatus.CORRECT : CorrectStatus.WRONG;
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

        memberPostcard.updateFreePostcardCount(memberPostcard.getFreePostcardCount() - 1);
        Postcard postcard = Postcard.builder()
                .sendMember(member)
                .receiveMember(targetMember)
                .postcardStatus(status)
                .memberReply(memberReply)
                .postcardType(postcardTypeRepository.findById(sendPostcardRequest.getPostcardTypeId())
                        .orElseThrow(() -> new IllegalArgumentException("PostcardType not found")))
                .imageUrl(sendPostcardRequest.getImageUrl())
                .build();
        postcardRepository.save(postcard);

        for (QuizReply quizReply : correctReplies) {
            quizReply.updatePostcard(postcard);
            quizReplyRepository.save(quizReply);
        }

        return SendPostcardResponse.builder().isSendSuccess(isCorrect).build();
    }

    @Override
    public PostcardTypeResponse getPostcardTypeList() {
        List<PostcardType> postcardTypes = postcardTypeRepository.findAll();
        List<PostcardTypeResponse.PostcardTypeDto> postcardTypeDtos = postcardTypes.stream()
                .map(type -> new PostcardTypeResponse.PostcardTypeDto(type.getId(), type.getName(), String.valueOf(type.getPrice())))
                .collect(Collectors.toList());
        return new PostcardTypeResponse(postcardTypeDtos);
    }

}
