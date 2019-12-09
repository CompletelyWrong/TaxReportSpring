package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.UserEntity;
import com.project.reportsystem.repository.UserRepository;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.mapper.UserMapper;
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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserServiceImpl.class)
public class UserServiceImplTest {
    private static final UserEntity USER_ENTITY = MOCK_USER_ENTITY;
    private static final User USER = MOCK_USER;
    private static final Inspector INSPECTOR = MOCK_INSPECTOR;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InspectorService inspectorService;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserServiceImpl userService;

    @After
    public void resetMocks() {
        reset(userMapper, userRepository, inspectorService, encoder);
    }

    @Test
    public void registerShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User is null");
        userService.register(null);
    }

    @Test
    public void findByIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User id is null");
        userService.findById(null);
    }

    @Test
    public void findAllShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Pageable is null");
        userService.findAll(null);
    }

    @Test
    public void updateInfoShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User is null");
        userService.updateInfo(null);
    }

    @Test
    public void changeInspectorForUserShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User is null");
        userService.changeInspectorForUser(null);
    }

    @Test
    public void findAllByInspectorIdShouldThrowIllegalArgumentException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Pageable/Inspector id is null");
        userService.findAllByInspectorId(null, null);
    }

    @Test
    public void registerShouldReturnUser() {
        when(userMapper.mapUserToUserEntityWithInspector(any(User.class), any(Inspector.class)))
                .thenReturn(USER_ENTITY);
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        when(inspectorService.findWithLessUsers())
                .thenReturn(INSPECTOR);

        User actual = userService.register(USER);
        verify(userRepository).save(any(UserEntity.class));

        assertThat(actual, is(USER));
    }

    @Test
    public void findByIdShouldReturnUser() {
        when(userMapper.mapUserEntityToUser(any(UserEntity.class)))
                .thenReturn(USER);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(USER_ENTITY));

        User actual = userService.findById(USER.getId());
        verify(userRepository).findById(anyLong());

        assertThat(actual, is(USER));
    }

    @Test
    public void findAllShouldReturnPage() {
        when(userMapper.mapUserEntityToUser(any(UserEntity.class)))
                .thenReturn(USER);
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(singletonList(USER_ENTITY)));

        Page<User> actual = userService.findAll(PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(USER));
    }

    @Test
    public void updateInfoShouldReturnUser() {
        when(userMapper.mapUserToUserEntity(any(User.class)))
                .thenReturn(USER_ENTITY);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(USER_ENTITY));

        User actual = userService.updateInfo(USER);
        verify(userRepository).save(any(UserEntity.class));

        assertThat(actual, is(USER));
    }

    @Test
    public void changeInspectorForUserShouldReturnUser() {
        when(userMapper.mapUserToUserEntityWithInspector(any(User.class), any(Inspector.class)))
                .thenReturn(USER_ENTITY);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(USER_ENTITY));
        when(inspectorService.findWithLessUsersExceptThisId(anyLong()))
                .thenReturn(INSPECTOR);

        User actual = userService.changeInspectorForUser(USER);
        verify(userRepository).save(any(UserEntity.class));

        assertThat(actual, is(USER));
    }

    @Test
    public void findAllByInspectorIdShouldReturnUser() {
        when(userMapper.mapUserEntityToUser(any(UserEntity.class)))
                .thenReturn(USER);
        when(userRepository.findAllByInspectorId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(singletonList(USER_ENTITY)));

        Page<User> actual = userService.findAllByInspectorId(INSPECTOR.getId(), PageRequest.of(0, 10));

        assertThat(actual.getContent(), hasItem(USER));
    }
}