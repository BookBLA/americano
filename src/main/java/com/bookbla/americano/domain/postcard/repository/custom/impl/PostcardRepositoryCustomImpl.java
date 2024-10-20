package com.bookbla.americano.domain.postcard.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;
import static com.bookbla.americano.domain.postcard.repository.entity.QPostcard.postcard;

@Repository
@RequiredArgsConstructor
public class PostcardRepositoryCustomImpl implements PostcardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostcardFromResponse> getPostcardsFromMember(Long memberId) {
        QMember member = QMember.member;
        QPostcard postcard = QPostcard.postcard;

        return queryFactory.select(Projections.fields(PostcardFromResponse.class
                        , postcard.receiveMember.id.as("memberId")
                        , postcard.receiveMember.memberProfile.name.as("memberName")
                        , postcard.receiveMember.memberProfile.birthDate.as("memberBirthDate")
                        , postcard.receiveMember.memberProfile.gender.as("memberGender")
                        , member.school.name.as("memberSchoolName")
                        , member.memberStyle.profileImageType.imageUrl.as("memberProfileImageUrl")
                        , postcard.id.as("postcardId")
                        , postcard.postcardStatus.as("postcardStatus"))
                )
                .from(member)
                .innerJoin(postcard).on(member.eq(postcard.receiveMember))
                .where(postcard.sendMember.id.eq(memberId))
                .orderBy(postcard.createdAt.desc())
                .fetch();
    }

    public List<PostcardToResponse> getPostcardsToMember(Long memberId) {
        QMember member = QMember.member;
        QPostcard postcard = QPostcard.postcard;

        return queryFactory.select(Projections.fields(PostcardToResponse.class
                        , postcard.id.as("postcardId")
                        , postcard.sendMember.id.as("memberId")
                        , postcard.sendMember.memberProfile.name.as("memberName")
                        , postcard.sendMember.memberProfile.birthDate.as("memberBirthDate")
                        , postcard.sendMember.memberProfile.gender.as("memberGender")
                        , member.school.name.as("memberSchoolName")
                        , member.memberStyle.profileImageType.imageUrl.as("memberProfileImageUrl")
                        , postcard.sendMember.memberStyle.smokeType
                        , postcard.sendMember.memberStyle.mbti
                        , postcard.createdAt.as("receivedTime")
                        , postcard.postcardStatus
                        , postcard.postcardType.imageUrl.as("postcardImageUrl"))
                )
                .from(member)
                .innerJoin(postcard).on(member.eq(postcard.sendMember))
                .where(postcard.receiveMember.id.eq(memberId)
                        .and(postcard.postcardStatus.eq(PostcardStatus.PENDING)))
                .orderBy(postcard.createdAt.desc())
                .fetch();
    }

    public List<Postcard> refuseExpiredPostcard() {
        QPostcard postcard = QPostcard.postcard;

        LocalDate now = LocalDate.now();
        LocalDateTime expireDate = now.minusDays(2).atStartOfDay();

        return queryFactory.selectFrom(postcard)
                .where(postcard.createdAt.before(expireDate)
                        .and(postcard.postcardStatus.eq(PostcardStatus.PENDING)
                                .or(postcard.postcardStatus.eq(PostcardStatus.READ))))
                .fetch();
    }

    public List<MatchedInfo> getReceiveIdsAndReceiveMemberBookIdsByRefusedAt(MemberRecommendationDto memberRecommendationDto) {

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        return queryFactory
                .select(postcard.receiveMember.id, postcard.receiveMemberBook.id)
                .from(postcard)
                .where(postcard.sendMember.id.eq(memberRecommendationDto.getMemberId()),
                        postcard.postcardStatus.eq(PostcardStatus.REFUSED),
                        postcard.postcardStatusRefusedAt.after(twoWeeksAgo))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(memberRecommendationDto.getMemberId(), tuple.get(postcard.receiveMember.id), tuple.get(postcard.receiveMemberBook.id), memberRecommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }
}
