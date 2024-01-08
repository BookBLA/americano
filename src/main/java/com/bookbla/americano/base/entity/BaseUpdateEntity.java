package com.bookbla.americano.base.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUpdateEntity {

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    //todo 인증방식에 따라 바뀔 소요가 있음
    //    @CreatedBy
//    @Column(nullable = false)
//    private String createdBy = null;

//    @LastModifiedBy
//    @Column(nullable = false)
//    private String lastModifiedBy = null;
}
