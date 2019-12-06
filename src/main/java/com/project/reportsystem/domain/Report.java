package com.project.reportsystem.domain;

import com.project.reportsystem.entity.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Long id;

    private User user;

    private String fileLink;

    private LocalDateTime creationDate;

    private ReportStatus status;
}
