package com.project.reportsystem.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractUser {
    @NotNull(message = "Please provide innCode")
    private Integer identificationCode;

    private Inspector inspector;
}
