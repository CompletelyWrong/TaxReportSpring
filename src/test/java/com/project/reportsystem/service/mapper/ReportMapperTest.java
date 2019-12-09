package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.ReportEntity;
import org.junit.Test;

import static com.project.reportsystem.MockData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ReportMapperTest {
    private static final Report REPORT = MOCK_REPORT;
    private static final User USER = MOCK_USER;
    private static final ReportEntity ENTITY = MOCK_REPORT_ENTITY;

    private final ReportMapper mapper = new ReportMapper();

    @Test
    void mapReportToReportEntityShouldReturnEntity() {
        final ReportEntity actual = mapper.mapReportToReportEntity(REPORT);

        assertThat(actual.getId(), is(REPORT.getId()));
        assertThat(actual.getCreationDate(), is(REPORT.getCreationDate()));
        assertThat(actual.getEntityUser().getId(), is(REPORT.getUser().getId()));
        assertThat(actual.getFileLink(), is(REPORT.getFileLink()));
        assertThat(actual.getStatus(), is(REPORT.getStatus()));
    }

    @Test
    void mapReportToReportEntityWithUserShouldReturnReportEntity() {
        final ReportEntity actual = mapper.mapReportToReportEntity(REPORT, USER);

        assertThat(actual.getId(), is(REPORT.getId()));
        assertThat(actual.getCreationDate(), is(REPORT.getCreationDate()));
        assertThat(actual.getEntityUser().getId(), is(REPORT.getUser().getId()));
        assertThat(actual.getFileLink(), is(REPORT.getFileLink()));
        assertThat(actual.getStatus(), is(REPORT.getStatus()));
    }

    @Test
    void mapReportEntityToReportShouldReturnReport() {
        final Report actual = mapper.mapReportEntityToReport(ENTITY);

        assertThat(actual.getId(), is(ENTITY.getId()));
        assertThat(actual.getCreationDate(), is(ENTITY.getCreationDate()));
        assertThat(actual.getUser().getId(), is(ENTITY.getEntityUser().getId()));
        assertThat(actual.getFileLink(), is(ENTITY.getFileLink()));
        assertThat(actual.getStatus(), is(ENTITY.getStatus()));
    }
}