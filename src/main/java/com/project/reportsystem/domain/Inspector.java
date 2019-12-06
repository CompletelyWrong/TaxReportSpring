package com.project.reportsystem.domain;

import com.project.reportsystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inspector {
    private Long id;

    @NotEmpty(message = "Please provide email")
    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,4}$", message = "Email does not match pattern: example@gmail.com")
    private String email;

    @NotEmpty(message = "Please provide password")
    @Pattern(regexp = "[\\d]{6,15}", message = "Password should be 6 to 15 characters long")
    private String password;

    @NotEmpty(message = "Please provide name")
    @Pattern(regexp = "[A-Za-zА-Яа-яїіІЇ]{2,30}", message = "Name should be at least 2 characters long and should not contain numbers")
    private String name;

    @NotEmpty(message = "Please provide surname")
    @Pattern(regexp = "[A-Za-zА-Яа-яїіІЇ]{2,30}", message = "Surname should be at least 2 characters long and should not contain numbers")
    private String surname;

    @NotEmpty(message = "Please provide patronymic")
    @Pattern(regexp = "[A-Za-zА-Яа-яїіІЇ]{2,30}", message = "Patronymic should be at least 2 characters long and should not contain numbers")
    private String patronymic;

    private Role role;
}
