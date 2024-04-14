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
import com.bookbla.americano.domain.postcard.service.PostcardService;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import com.bookbla.americano.domain.quiz.enums.CorrectStatus;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import com.bookbla.americano.domain.quiz.repository.QuizReplyRepository;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import com.bookbla.americano.domain.quiz.repository.entity.QuizReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        MemberAsk memberAsk =  memberAskRepository.findById(sendPostcardRequest.getMemberAskId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 memberAskId 입니다."));

        MemberReply memberReply = MemberReply.builder()
                .memberAsk(memberAsk)
                .content(sendPostcardRequest.getMemberReply())
                .build();

        memberReplyRepository.save(memberReply);

        boolean isCorrectFlag = false;
        Member member = memberRepository.getByIdOrThrow(memberId);
        MemberPostcard memberPostcard = memberPostcardRepository.findMemberPostcardByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("memberPostcard가 존재하지 않습니다."));

        if (memberPostcard.getFreePostcardCount() == 0) {
            return SendPostcardResponse.builder().isSendSuccess(false).build();
        }

        Member targetMember = memberRepository.getByIdOrThrow(memberAsk.getMember().getId());

        for (SendPostcardRequest.QuizAnswer quizAnswer : sendPostcardRequest.getQuizAnswerList()) {
            QuizQuestion quizQuestion = quizQuestionRepository.findById(quizAnswer.getQuizId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 quizId 입니다."));

            if (quizQuestion.getFirstChoice().equals(quizAnswer.getQuizAnswer())) {
                QuizReply quizReply = QuizReply.builder()
                        .quizQuestion(quizQuestion)
                        .answer(quizAnswer.getQuizAnswer())
                        .member(member)
                        .correctStatus(CorrectStatus.CORRECT)
                        .build();
                quizReplyRepository.save(quizReply);
                isCorrectFlag = true;
            } else {
                QuizReply quizReply = QuizReply.builder()
                        .quizQuestion(quizQuestion)
                        .answer(quizAnswer.getQuizAnswer())
                        .member(member)
                        .correctStatus(CorrectStatus.WRONG)
                        .build();
                quizReplyRepository.save(quizReply);
            }
        }

        if (isCorrectFlag) {
            memberPostcard.updateFreePostcardCount(memberPostcard.getFreePostcardCount() - 1);

            Postcard postcard = Postcard.builder()
                    .sendMember(member)
                    .receiveMember(targetMember)
                    .postcardStatus(PostcardStatus.PENDING)
                    .memberReply(memberReply)
                    .postcardType(postcardTypeRepository.findById(sendPostcardRequest.getPostcardTypeId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 postcardTypeId 입니다."))
                    )
                    .imageUrl(sendPostcardRequest.getImageUrl())
                    .build();
            postcardRepository.save(postcard);

            return SendPostcardResponse.builder().isSendSuccess(true).build();

        } else {
            return SendPostcardResponse.builder().isSendSuccess(false).build();
        }
    }
}
