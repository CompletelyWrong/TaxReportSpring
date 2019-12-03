package com.project.reportsystem.service;

import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActionService {
    Action addActionToReport(Action action, Report report);

    Page<Action> findAllForReportById(Long reportId, Pageable pageable);
}
