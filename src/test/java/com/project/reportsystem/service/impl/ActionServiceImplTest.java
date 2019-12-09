package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Report;
import com.project.reportsystem.entity.ActionEntity;
import com.project.reportsystem.repository.ActionRepository;
import com.project.reportsystem.service.mapper.ActionMapper;
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

import static com.project.reportsystem.MockData.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ActionServiceImpl.class)
public class ActionServiceImplTest {
    private static final Action ACTION = MOCK_ACTION;
    private static final ActionEntity ACTION_ENTITY = MOCK_ACTION_ENTITY;
    private static final Report REPORT = MOCK_REPORT;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private ActionMapper actionMapper;

    @MockBean
    private ActionRepository actionRepository;

    @Autowired
    private ActionServiceImpl actionService;

    @After
    public void resetMocks() {
        reset(actionMapper, actionRepository);
    }

    @Test
    public void addActionToReportShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Action/Report is null");
        actionService.addActionToReport(null, null);
    }

    @Test
    public void findAllForReportByIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Report id/Pageable is null");
        actionService.findAllForReportById(null, null);
    }

    @Test
    public void addActionToReportShouldReturnReport() {
        when(actionMapper.mapActionToActionEntity(any(Action.class), any(Report.class)))
                .thenReturn(ACTION_ENTITY);

        Action actual = actionService.addActionToReport(ACTION, REPORT);
        verify(actionRepository).save(any(ActionEntity.class));

        assertThat(actual, is(ACTION));
    }

    @Test
    public void findAllForReportByIdShouldReturnPage() {
        when(actionMapper.mapActionEntityToAction(any(ActionEntity.class))).thenReturn(ACTION);
        when(actionRepository.findAllByReportEntityId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(singletonList(ACTION_ENTITY)));

        Page<Action> actual = actionService.findAllForReportById(1L, PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(ACTION));
    }
}