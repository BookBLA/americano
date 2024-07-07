package com.bookbla.americano.domain.school.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class School extends BaseUpdateEntity {

    public static final int OPEN_STANDARD = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String emailDomain;

    @Builder.Default
    @Enumerated(value = EnumType.STRING)
    private SchoolStatus schoolStatus = SchoolStatus.CLOSED;

    public static School notRegistered() {
        return School.builder().name("등록되지 않음").build();
    }

    public void checkOpen() {
        if (schoolStatus == SchoolStatus.OPEN) {
            return;
        }
    }

    public int getOpenStandard() {
        return OPEN_STANDARD;
    }

    public int validMemberCounts() {
        return 1;
    }
}
