package com.bookbla.americano.domain.school;

import java.util.ArrayList;
import java.util.List;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import com.bookbla.americano.domain.member.repository.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class School extends BaseUpdateEntity {

    private static final int OPEN_STANDARD = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String emailPostfix;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private List<Member> members = new ArrayList<>();

    @Column(nullable = false)
    private boolean isOpen = false;

    public void updateOpen() {
        if (members.size() >= OPEN_STANDARD) {
            isOpen = true;
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
