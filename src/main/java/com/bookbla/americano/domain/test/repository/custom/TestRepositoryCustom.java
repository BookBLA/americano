package com.bookbla.americano.domain.test.repository.custom;

import com.bookbla.americano.domain.test.repository.entity.TestEntity;

import java.util.List;

public interface TestRepositoryCustom {
    List<TestEntity> findByContents(String contents);
}
