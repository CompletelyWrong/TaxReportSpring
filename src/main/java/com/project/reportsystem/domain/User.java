package com.project.reportsystem.domain;

import com.project.reportsystem.entity.Role;
import lombok.*;


@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.PUBLIC)
    private Long id;
    @Setter(AccessLevel.PUBLIC)
    private String email;
    @Setter(AccessLevel.PUBLIC)
    private String password;
    @Setter(AccessLevel.PUBLIC)
    private String name;
    @Setter(AccessLevel.PUBLIC)
    private String surname;
    @Setter(AccessLevel.PUBLIC)
    private String patronymic;
    @Setter(AccessLevel.PUBLIC)
    private Role role;
    @Setter(AccessLevel.PUBLIC)
    private Integer identificationCode;
}
