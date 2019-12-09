package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.ReportEntity;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.exception.InvalidAddEntityException;
import com.project.reportsystem.repository.ReportRepository;
import com.project.reportsystem.service.ReportService;
import com.project.reportsystem.service.mapper.ReportMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper mapper;

    @Override
    public Report addReportToUser(Report report, User user) {
        if (Objects.isNull(report) || Objects.isNull(user)) {
            log.warn("Report or User is null");
            throw new IllegalArgumentException("Report or User is null");
        }

        reportRepository.save(mapper.mapReportToReportEntity(report, user));

        return report;
    }

    @Override
    public Report findById(Long reportId) {
        if (Objects.isNull(reportId)) {
            log.warn("Report id is null");
            throw new IllegalArgumentException("Report id is null");
        }

        ReportEntity reportEntity = reportRepository.findById(reportId).orElseThrow(() -> {
            log.warn("There is no report with this such id");
            return new EntityNotFoundException("There is no report with this such id");
        });

        return mapper.mapReportEntityToReport(reportEntity);
    }

    @Override
    public Report updateReport(Report report) {
        if (Objects.isNull(report)) {
            log.warn("Report is null");
            throw new IllegalArgumentException("Report is null");
        }

        reportRepository.save(mapper.mapReportToReportEntity(report));

        return report;
    }

    @Override
    public Page<Report> findAllByUserId(Long userId, Pageable pageable) {
        if (Objects.isNull(userId) || Objects.isNull(pageable)) {
            log.warn("User id or Pageable is null");
            throw new IllegalArgumentException("User id or Pageable is null");
        }

        return reportRepository.findByEntityUserId(userId, pageable)
                .map(mapper::mapReportEntityToReport);
    }
}
