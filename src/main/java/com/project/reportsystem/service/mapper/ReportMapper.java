package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.ReportEntity;
import com.project.reportsystem.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public ReportEntity mapReportToReportEntity(Report report, User user) {
        return ReportEntity.builder()
                .creationDate(report.getCreationDate())
                .fileLink(report.getFileLink())
                .id(report.getId())
                .status(report.getStatus())
                .entityUser(UserEntity.builder()
                        .id(user.getId())
                        .build())
                .build();
    }

    public ReportEntity mapReportToReportEntity(Report report, Long reportId) {
        return ReportEntity.builder()
                .creationDate(report.getCreationDate())
                .fileLink(report.getFileLink())
                .id(reportId)
                .status(report.getStatus())
                .entityUser(UserEntity.builder()
                        .id(report.getUser().getId())
                        .build())
                .build();
    }

    public Report mapReportEntityToReport(ReportEntity entity) {
        return Report.builder()
                .creationDate(entity.getCreationDate())
                .fileLink(entity.getFileLink())
                .id(entity.getId())
                .status(entity.getStatus())
                .user(User.builder()
                        .id(entity.getEntityUser().getId())
                        .build())
                .build();
    }
}
