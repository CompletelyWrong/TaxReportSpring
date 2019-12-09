package com.project.reportsystem.service.mapper;

import com.project.reportsystem.MockData;
import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Report;
import com.project.reportsystem.entity.ActionEntity;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ActionMapperTest {
    private static final Report REPORT = MockData.MOCK_REPORT;
    private static final Action ACTION = MockData.MOCK_ACTION;
    private static final ActionEntity ENTITY = MockData.MOCK_ACTION_ENTITY;

    private final ActionMapper mapper = new ActionMapper();

    @Test
    void mapActionToActionEntityWithReportShouldReturnEntity() {
        final ActionEntity actual = mapper.mapActionToActionEntity(ACTION, REPORT);

        assertThat(actual.getId(), is(ACTION.getId()));
        assertThat(actual.getMessage(), is(ACTION.getMessage()));
        assertThat(actual.getAction(), is(ACTION.getActionType()));
        assertThat(actual.getDateTime(), is(ACTION.getDateTime()));
        assertThat(actual.getInspectorEntity().getId(), is(ACTION.getInspector().getId()));

    }

    @Test
    void mapActionEntityToActionShouldReturnAction() {
        final Action actual = mapper.mapActionEntityToAction(ENTITY);

        assertThat(actual.getId(), is(ENTITY.getId()));
        assertThat(actual.getMessage(), is(ENTITY.getMessage()));
        assertThat(actual.getActionType(), is(ENTITY.getAction()));
        assertThat(actual.getDateTime(), is(ENTITY.getDateTime()));
        assertThat(actual.getInspector().getId(), is(ENTITY.getInspectorEntity().getId()));
    }
}