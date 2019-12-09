package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.UserEntity;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.repository.InspectorRepository;
import com.project.reportsystem.repository.UserRepository;
import com.project.reportsystem.service.mapper.InspectorMapper;
import com.project.reportsystem.service.mapper.UserMapper;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.project.reportsystem.MockData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CustomUserDetailsServiceImpl.class)
public class CustomUserDetailsServiceImplTest {

    private static final User USER = MOCK_USER;
    private static final UserEntity USER_ENTITY = MOCK_USER_ENTITY;
    private static final Inspector INSPECTOR = MOCK_INSPECTOR;
    private static final InspectorEntity INSPECTOR_ENTITY = MOCK_INSPECTOR_ENTITY;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private InspectorMapper inspectorMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InspectorRepository inspectorRepository;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @After
    public void resetMocks() {
        reset(userRepository, userMapper, inspectorMapper, inspectorRepository);
    }

    @Test
    public void loadUserByUsernameShouldReturnUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(USER_ENTITY));
        when(userMapper.mapUserEntityToUser(any(UserEntity.class))).thenReturn(USER);

        final UserDetails actual = customUserDetailsService.loadUserByUsername("email@gmail.com");

        assertThat(actual.getUsername(), is(USER.getEmail()));
        assertThat(actual.getAuthorities().size(), is(1));
    }

    @Test
    public void loadUserByUsernameShouldReturnInspector() {
        when(inspectorRepository.findByEmail(anyString())).thenReturn(Optional.of(INSPECTOR_ENTITY));
        when(inspectorMapper.mapInspectorEntityToInspector(any(InspectorEntity.class))).thenReturn(INSPECTOR);

        final UserDetails actual = customUserDetailsService.loadUserByUsername("email@gmail.com");

        assertThat(actual.getUsername(), is(INSPECTOR.getEmail()));
        assertThat(actual.getAuthorities().size(), is(1));
    }

    @Test
    public void loadUserByUsernameShouldThrowEntityNotFoundException() {
        expectedException.expect(EntityNotFoundException.class);
        expectedException.expectMessage("No user found with such email");
        customUserDetailsService.loadUserByUsername("email@gmail.com");

    }

    @Test
    public void loadUserByUsernameShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Email id is null");
        customUserDetailsService.loadUserByUsername(null);
    }
}