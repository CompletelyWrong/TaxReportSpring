package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.repository.InspectorRepository;
import com.project.reportsystem.service.mapper.InspectorMapper;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@ContextConfiguration(classes = InspectorServiceImpl.class)
public class InspectorServiceImplTest {
    private static final Inspector INSPECTOR = MOCK_INSPECTOR;
    private static final InspectorEntity INSPECTOR_ENTITY = MOCK_INSPECTOR_ENTITY;
    private static final User USER = MOCK_USER;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private InspectorMapper inspectorMapper;

    @MockBean
    private InspectorRepository inspectorRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    private InspectorServiceImpl inspectorService;

    @After
    public void resetMocks() {
        reset(inspectorMapper, inspectorRepository, encoder);
    }

    @Test
    public void createInspectorShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Inspector is null");
        inspectorService.createInspector(null);
    }

    @Test
    public void updateInfoShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Inspector is null");
        inspectorService.updateInfo(null);
    }

    @Test
    public void findByUserIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Inspector is null");
        inspectorService.findByUserId(null);
    }

    @Test
    public void findAllShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Pageable is null");
        inspectorService.findAll(null);
    }

    @Test
    public void findWithLessUsersExceptThisIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Inspector id is null");
        inspectorService.findWithLessUsersExceptThisId(null);
    }

    @Test
    public void createInspectorShouldReturnInspector() {
        when(inspectorMapper.mapInspectorToInspectorEntity(any(Inspector.class)))
                .thenReturn(INSPECTOR_ENTITY);

        Inspector actual = inspectorService.createInspector(INSPECTOR);
        verify(inspectorRepository).save(any(InspectorEntity.class));

        assertThat(actual, is(INSPECTOR));
    }

    @Test
    public void updateInfoShouldReturnInspector() {
        when(inspectorMapper.mapInspectorToInspectorEntity(any(Inspector.class)))
                .thenReturn(INSPECTOR_ENTITY);
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class)))
                .thenReturn(INSPECTOR);
        when(inspectorRepository.findById(anyLong()))
                .thenReturn(Optional.of(INSPECTOR_ENTITY));

        Inspector actual = inspectorService.updateInfo(INSPECTOR);
        verify(inspectorRepository).save(any(InspectorEntity.class));

        assertThat(actual, is(INSPECTOR));
    }

    @Test
    public void findByUserIdShouldReturnInspector() {
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class)))
                .thenReturn(INSPECTOR);

        when(inspectorRepository.findByUserId(anyLong()))
                .thenReturn(Optional.of(INSPECTOR_ENTITY));

        Inspector actual = inspectorService.findByUserId(USER.getId());
        verify(inspectorRepository).findByUserId(anyLong());

        assertThat(actual, is(INSPECTOR));
    }

    @Test
    public void findAllShouldReturnPage() {
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class)))
                .thenReturn(INSPECTOR);
        when(inspectorRepository.findAllByRole(any(Pageable.class), any(Role.class)))
                .thenReturn(new PageImpl<>(singletonList(INSPECTOR_ENTITY)));

        Page<Inspector> actual = inspectorService.findAll(PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(INSPECTOR));
    }

    @Test
    public void findWithLessUsersExceptThisIdShouldReturnInspector() {
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class)))
                .thenReturn(INSPECTOR);

        when(inspectorRepository.findWithLessUsersExceptThisId(anyLong()))
                .thenReturn(Optional.of(INSPECTOR_ENTITY));

        Inspector actual = inspectorService.findWithLessUsersExceptThisId(INSPECTOR.getId());
        verify(inspectorRepository).findWithLessUsersExceptThisId(anyLong());

        assertThat(actual, is(INSPECTOR));
    }

    @Test
    public void findWithLessUsersReturnInspector() {
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class)))
                .thenReturn(INSPECTOR);

        when(inspectorRepository.findWithLessUsers())
                .thenReturn(Optional.of(INSPECTOR_ENTITY));

        Inspector actual = inspectorService.findWithLessUsers();
        verify(inspectorRepository).findWithLessUsers();

        assertThat(actual, is(INSPECTOR));
    }
}