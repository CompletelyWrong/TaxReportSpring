package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.Report;
import com.project.reportsystem.entity.ActionEntity;
import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.ReportEntity;
import org.springframework.stereotype.Component;

@Component
public class ActionMapper {
    public ActionEntity mapActionToActionEntity(Action action, Report report) {
        return ActionEntity.builder()
                .action(action.getAction())
                .dateTime(action.getDateTime())
                .id(action.getId())
                .inspectorEntity(InspectorEntity.builder()
                        .id(action.getInspector().getId())
                        .build())
                .message(action.getMessage())
                .reportEntity(ReportEntity.builder()
                        .id(report.getId())
                        .build())
                .build();
    }

    public Action mapActionEntityToAction(ActionEntity entity) {
        return Action.builder()
                .action(entity.getAction())
                .dateTime(entity.getDateTime())
                .id(entity.getId())
                .inspector(Inspector.builder()
                        .id(entity.getInspectorEntity().getId())
                        .build())
                .message(entity.getMessage())
                .report(Report.builder()
                        .id(entity.getReportEntity().getId())
                        .build())
                .build();
    }
}
