package com.bookbla.americano.domain.test.repository;

import com.bookbla.americano.domain.test.repository.custom.TestRepositoryCustom;
import com.bookbla.americano.domain.test.repository.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long>, TestRepositoryCustom {
}
