package com.bookbla.americano.domain.test.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "test")
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contents")
    private String contents;
}
