package com.bookbla.americano.domain.school.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
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

import java.util.Objects;

import static com.bookbla.americano.domain.school.repository.entity.SchoolStatus.CLOSED;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class School extends BaseEntity {

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
    private SchoolStatus schoolStatus = CLOSED;

    public static School notRegistered() {
        return School.builder().name("등록되지 않음").schoolStatus(CLOSED).build();
    }

    public void checkOpen(int currentMemberCounts) {
        if (currentMemberCounts >= OPEN_STANDARD) {
            this.schoolStatus = SchoolStatus.OPEN;
        }
    }

    public int getOpenStandard() {
        return OPEN_STANDARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
