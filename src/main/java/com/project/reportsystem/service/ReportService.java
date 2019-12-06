package com.project.reportsystem.service;

import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    Report addReportToUser(Report report, User user);

    Report findById(Long reportId);

    Report updateReport(Report report);

    Page<Report> findAllByUserId(Long userId, Pageable pageable);
}
