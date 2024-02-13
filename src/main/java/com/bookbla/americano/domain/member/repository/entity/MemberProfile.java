package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.repository.entity.enums.Gender;
import com.bookbla.americano.domain.member.service.dto.MemberDto;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String major;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String profileImageUrl;

    private int studentNumber;

    private String schoolName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String nickname;

    private String openKakaoRoomUrl;
}