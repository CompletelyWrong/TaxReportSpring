package com.project.reportsystem.domain;

import com.project.reportsystem.entity.ReportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Report {
    @Setter(AccessLevel.PUBLIC)
    private Long id;
    @Setter(AccessLevel.PUBLIC)
    private User user;
    @Setter(AccessLevel.PUBLIC)
    private String fileLink;
    @Setter(AccessLevel.PUBLIC)
    private LocalDateTime creationDate;
    @Setter(AccessLevel.PUBLIC)
    private ReportStatus status;
}
