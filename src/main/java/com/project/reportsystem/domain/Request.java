package com.project.reportsystem.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Request {
    private Long id;
    @Setter(AccessLevel.PUBLIC)
    private User user;
    @Setter(AccessLevel.PUBLIC)
    private String reason;
}
