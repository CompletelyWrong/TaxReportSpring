package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Report;
import com.project.reportsystem.repository.ActionRepository;
import com.project.reportsystem.service.ActionService;
import com.project.reportsystem.service.mapper.ActionMapper;
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
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    private final ActionMapper mapper;

    @Override
    public Action addActionToReport(Action action, Report report) {
        if (Objects.isNull(action) || Objects.isNull(report)) {
            log.warn("Action/Report is null");
            throw new IllegalArgumentException("Action/Report is null");
        }

        actionRepository.save(mapper.mapActionToActionEntity(action, report));

        return action;
    }

    @Override
    public Page<Action> findAllForReportById(Long reportId, Pageable pageable) {
        if (Objects.isNull(reportId) || Objects.isNull(pageable)) {
            log.warn("Report id/Pageable is null");
            throw new IllegalArgumentException("Report id/Pageable is null");
        }

        return actionRepository.findAllByReportEntityId(reportId, pageable)
                .map(mapper::mapActionEntityToAction);
    }
}
