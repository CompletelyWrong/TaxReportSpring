package com.project.reportsystem.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "surname", length = 50, nullable = false)
    private String surname;

    @Column(name = "patronymic", length = 50, nullable = false)
    private String patronymic;

    @Column(name = "password", length = 70, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
