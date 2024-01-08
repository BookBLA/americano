package com.bookbla.americano.base.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseInsertEntity {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    //todo 추후 적용
//    @CreatedBy
//    @Column(nullable = false)
//    private String createdBy = null;
}
