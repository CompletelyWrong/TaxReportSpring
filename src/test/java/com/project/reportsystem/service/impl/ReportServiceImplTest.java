package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.ReportEntity;
import com.project.reportsystem.repository.ReportRepository;
import com.project.reportsystem.service.mapper.ReportMapper;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.project.reportsystem.MockData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ReportServiceImpl.class)
public class ReportServiceImplTest {
    private static final Report REPORT = MOCK_REPORT;
    private static final ReportEntity REPORT_ENTITY = MOCK_REPORT_ENTITY;
    private static final User USER = MOCK_USER;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private ReportMapper reportMapper;

    @MockBean
    private ReportRepository reportRepository;

    @Autowired
    private ReportServiceImpl reportService;

    @After
    public void resetMocks() {
        reset(reportMapper, reportRepository);
    }

    @Test
    public void addReportToUserShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Report or User is null");
        reportService.addReportToUser(null, null);
    }

    @Test
    public void findByIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Report id is null");
        reportService.findById(null);
    }

    @Test
    public void updateReportShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Report is null");
        reportService.updateReport(null);
    }

    @Test
    public void findAllByUserIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User id or Pageable is null");
        reportService.findAllByUserId(null, null);
    }

    @Test
    public void addReportToUserShouldReturnReport() {
        when(reportMapper.mapReportToReportEntity(any(Report.class), any(User.class)))
                .thenReturn(REPORT_ENTITY);

        Report actual = reportService.addReportToUser(REPORT, USER);
        verify(reportRepository).save(any(ReportEntity.class));

        assertThat(actual, is(REPORT));
    }

    @Test
    public void findByIdShouldReturnReport() {
        when(reportMapper.mapReportEntityToReport(any(ReportEntity.class)))
                .thenReturn(REPORT);

        when(reportRepository.findById(anyLong()))
                .thenReturn(Optional.of(REPORT_ENTITY));

        Report actual = reportService.findById(REPORT.getId());
        verify(reportRepository).findById(anyLong());

        assertThat(actual, is(REPORT));
    }

    @Test
    public void updateReportShouldReturnReport() {
        when(reportMapper.mapReportToReportEntity(any(Report.class)))
                .thenReturn(REPORT_ENTITY);

        Report actual = reportService.updateReport(REPORT);
        verify(reportRepository).save(any(ReportEntity.class));

        assertThat(actual, is(REPORT));
    }

    @Test
    public void findAllByUserIdShouldReturnPage() {
        when(reportMapper.mapReportEntityToReport(any(ReportEntity.class))).thenReturn(REPORT);
        when(reportRepository.findByEntityUserId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(singletonList(REPORT_ENTITY)));

        Page<Report> actual = reportService.findAllByUserId(1L, PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(REPORT));
    }
}