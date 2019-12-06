package com.project.reportsystem.service.mapper;


import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity mapUserToUserEntity(User user) {
        return UserEntity.builder()
                .identificationCode(user.getIdentificationCode())
                .role(user.getRole())
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .password(user.getPassword())
                .patronymic(user.getPatronymic())
                .surname(user.getSurname())
                .inspector(InspectorEntity.builder().id(user.getInspector().getId()).build())
                .build();
    }

    public User mapUserEntityToUser(UserEntity entityUser) {
        return User.builder()
                .identificationCode(entityUser.getIdentificationCode())
                .role(entityUser.getRole())
                .email(entityUser.getEmail())
                .name(entityUser.getName())
                .id(entityUser.getId())
                .password(entityUser.getPassword())
                .patronymic(entityUser.getPatronymic())
                .surname(entityUser.getSurname())
                .inspector(Inspector.builder().id(entityUser.getInspector().getId()).build())
                .build();
    }

    public UserEntity mapUserToUserEntityWithInspector(User user, Inspector inspector) {
        return UserEntity.builder()
                .identificationCode(user.getIdentificationCode())
                .role(user.getRole())
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .password(user.getPassword())
                .patronymic(user.getPatronymic())
                .surname(user.getSurname())
                .inspector(InspectorEntity.builder().id(inspector.getId()).build())
                .build();
    }
}
