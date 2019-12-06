package com.project.reportsystem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private Long id;

    private User user;

    @NotEmpty(message = "Please provide reason")
    private String reason;
}
