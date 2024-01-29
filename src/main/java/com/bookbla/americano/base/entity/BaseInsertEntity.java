package com.bookbla.americano.base.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseInsertEntity {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @CreatedBy
    @Column(nullable = false)
    private String createdBy = null;
}
