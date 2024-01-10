package com.bookbla.americano.domain.test.repository.custom.impl;

import static com.bookbla.americano.domain.test.repository.entity.QTestEntity.testEntity;

import com.bookbla.americano.domain.test.repository.custom.TestRepositoryCustom;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRepositoryCustomImpl implements TestRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<TestEntity> findByContents(String contents) {
        return queryFactory
                .selectFrom(testEntity)
                .where(testEntity.contents.eq(contents))
                .fetch();
    }
}
