package com.bookbla.americano.domain.test.repository.custom.impl;

import com.bookbla.americano.domain.test.repository.custom.TestRepositoryCustom;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookbla.americano.domain.test.repository.entity.QTestEntity.testEntity;

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
