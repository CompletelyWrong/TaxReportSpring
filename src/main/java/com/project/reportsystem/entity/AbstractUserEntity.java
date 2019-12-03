package com.project.reportsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor(force = true)
@SuperBuilder
@MappedSuperclass
public abstract class AbstractUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(name = "email", length = 50, nullable = false)
    private final String email;

    @Column(name = "name", length = 50, nullable = false)
    private final String name;

    @Column(name = "surname", length = 50, nullable = false)
    private final String surname;

    @Column(name = "patronymic", length = 50, nullable = false)
    private final String patronymic;

    @Column(name = "password", length = 70, nullable = false)
    private final String password;
    @Enumerated(EnumType.STRING)
    private final Role role;
}
