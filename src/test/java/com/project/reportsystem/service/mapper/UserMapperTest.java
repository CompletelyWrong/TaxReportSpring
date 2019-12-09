package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.UserEntity;
import org.junit.Test;

import static com.project.reportsystem.MockData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class UserMapperTest {
    private static final User USER = MOCK_USER;
    private static final UserEntity USER_ENTITY = MOCK_USER_ENTITY;
    private static final Inspector INSPECTOR = MOCK_INSPECTOR;

    private final UserMapper userMapper = new UserMapper();

    @Test
    public void mapUserToUserEntityShouldReturnUserEntity() {
        final UserEntity actual = userMapper.mapUserToUserEntity(USER);

        assertThat(actual.getEmail(), is(MOCK_USER.getEmail()));
        assertThat(actual.getPassword(), is(MOCK_USER.getPassword()));
        assertThat(actual.getName(), is(MOCK_USER.getName()));
        assertThat(actual.getSurname(), is(MOCK_USER.getSurname()));
        assertThat(actual.getRole(), is(MOCK_USER.getRole()));
        assertThat(actual.getPatronymic(), is(MOCK_USER.getPatronymic()));
    }

    @Test
    public void mapUserEntityToUserShouldReturnUser() {
        final User actual = userMapper.mapUserEntityToUser(USER_ENTITY);

        assertThat(actual.getId(), is(MOCK_USER_ENTITY.getId()));
        assertThat(actual.getEmail(), is(MOCK_USER_ENTITY.getEmail()));
        assertThat(actual.getPassword(), is(MOCK_USER_ENTITY.getPassword()));
        assertThat(actual.getName(), is(MOCK_USER_ENTITY.getName()));
        assertThat(actual.getSurname(), is(MOCK_USER_ENTITY.getSurname()));
        assertThat(actual.getRole(), is(MOCK_USER_ENTITY.getRole()));
        assertThat(actual.getPatronymic(), is(MOCK_USER.getPatronymic()));
    }

    @Test
    public void mapUserToUserEntityWithInspectorShouldReturnUserEntity() {
        final UserEntity actual = userMapper.mapUserToUserEntityWithInspector(USER, INSPECTOR);

        assertThat(actual.getId(), is(MOCK_USER_ENTITY.getId()));
        assertThat(actual.getEmail(), is(MOCK_USER_ENTITY.getEmail()));
        assertThat(actual.getPassword(), is(MOCK_USER_ENTITY.getPassword()));
        assertThat(actual.getName(), is(MOCK_USER_ENTITY.getName()));
        assertThat(actual.getSurname(), is(MOCK_USER_ENTITY.getSurname()));
        assertThat(actual.getRole(), is(MOCK_USER_ENTITY.getRole()));
        assertThat(actual.getPatronymic(), is(MOCK_USER.getPatronymic()));
    }
}