package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberIntroResponse;
import com.bookbla.americano.domain.member.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberMatchRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberMatch;
import com.bookbla.americano.domain.member.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMatchingService {

    final int MAX_RECOMMEND = 4; // 하루 최대 4명

    private final MemberRepository memberRepository;
    private final MemberBookRepository memberBookRepository;
    private final PostcardRepository postcardRepository;
    private final MemberMatchRepository memberMatchRepository;

    public List<MemberIntroResponse> getRecommendationList(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberBook> memberBooks = memberBookRepository.findByMemberOrderByCreatedAt(member);
        List<Postcard> postcards = postcardRepository.findBySendMember(member);

        member.updateLastUsedAt();

        //member, memberBooks로 dto생성
        MemberRecommendationDto memberRecommendationDto = MemberRecommendationDto.from(member, memberBooks);

        //dto와 potcards가지고 추천회원 id와 추천회원의 책 id 추출
        List<Map<Long, Long>> recommendationItems = memberRepository.getRecommendationMemberIdsAndBookIds(memberRecommendationDto, postcards);

        //추출한 추천회원 id와 추천회원의 책 id를 db에 저장
        List<MemberIntroResponse> memberIntroResponses = new ArrayList<>();
        for (Map<Long, Long> item : recommendationItems) {
            Map.Entry<Long, Long> entry = item.entrySet().iterator().next();
            Long recMemberId = entry.getKey();
            Long recMemberBookId = entry.getValue();

            if (recMemberId != null && recMemberBookId != null) {

                Member recMember = memberRepository.getByIdOrThrow(recMemberId);
                MemberBook recMemberBook = memberBookRepository.getByIdOrThrow(recMemberBookId);

                // MemberMatch 객체 생성
                MemberMatch memberMatch = MemberMatch.builder()
                        .appMember(member)
                        .matchedMember(recMember)
                        .matchedBook(recMemberBook.getBook())
                        .build();

                // MemberMatch 저장
                memberMatchRepository.save(memberMatch);

                MemberIntroResponse memberIntroResponse = MemberIntroResponse.from(recMember, recMemberBook);
                memberIntroResponses.add(memberIntroResponse);
            } else {
                throw new BaseException(MemberMatchingExceptionType.MATCHING_DOESNT_EXIST);
            }
        }

        return getDailyMemberIntroResponses(memberIntroResponses);
    }

    private List<MemberIntroResponse> getDailyMemberIntroResponses(List<MemberIntroResponse> memberIntroResponses) {
        if (memberIntroResponses.size() > MAX_RECOMMEND) {
            return memberIntroResponses.subList(0, MAX_RECOMMEND);
        } else if (memberIntroResponses.isEmpty()){
            throw new BaseException(MemberMatchingExceptionType.MATCHING_DOESNT_EXIST);
        }
        return memberIntroResponses;
    }
}