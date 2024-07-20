package com.bookbla.americano.domain.admin.repository.entity;


import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.admin.excpetion.AdminExceptionType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class Admin extends BaseEntity {

    private static final int MAX_FAIL_COUNT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    @Builder.Default
    private int failCount = 0;

    public boolean isDifferentPassword(PasswordEncoder passwordEncoder, String other) {
        return !passwordEncoder.matches(other, this.password);
    }

    public void increaseFailCount() {
        this.failCount++;
    }

    public void resetFailCount() {
        this.failCount = 0;
    }

    public void validateFailCount() {
        if (failCount >= MAX_FAIL_COUNT) {
            throw new BaseException(AdminExceptionType.FAIL_LOGIN_TRIAL);
        }
    }
}
