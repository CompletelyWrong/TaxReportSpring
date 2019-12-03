package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.entity.InspectorEntity;
import org.springframework.stereotype.Component;

@Component
public class InspectorMapper {
    public InspectorEntity mapInspectorToInspectorEntity(Inspector inspector) {
        return InspectorEntity.builder()
                .email(inspector.getEmail())
                .name(inspector.getName())
                .id(inspector.getId())
                .password(inspector.getPassword())
                .patronymic(inspector.getPatronymic())
                .surname(inspector.getSurname())
                .role(inspector.getRole())
                .build();
    }

    public Inspector mapInspectorEntityToInspector(InspectorEntity entity) {
        return Inspector.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .id(entity.getId())
                .password(entity.getPassword())
                .patronymic(entity.getPatronymic())
                .surname(entity.getSurname())
                .role(entity.getRole())
                .build();
    }
}
