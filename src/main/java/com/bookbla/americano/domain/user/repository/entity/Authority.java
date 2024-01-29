package com.bookbla.americano.domain.user.repository.entity;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authority")
public class Authority extends BaseUpdateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id", nullable = false)
    private long id;

    @NotNull
    @Column(name = "authority_name", nullable = false, length = 20)
    private String authorityName;
}
