package com.bookbla.americano.domain.user.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User extends BaseUpdateEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "sex", length = 10)
    private String sex;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "profile_image", length = 100)
    private String profileImage;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserAuthority> userAuthoritySet = new HashSet<>();
}
