package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.domain.member.repository.custom.MemberBookRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static com.bookbla.americano.domain.book.repository.entity.QBook.book;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class MemberBookRepositoryImpl implements MemberBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberBook> findAllByMemberBookId(Collection<Long> memberBookIds) {
        return queryFactory
                .selectFrom(memberBook)
                .join(memberBook.book, book).fetchJoin()
                .where(memberBook.id.in(memberBookIds))
                .fetch();
    }
}
