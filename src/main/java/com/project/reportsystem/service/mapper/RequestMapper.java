package com.project.reportsystem.service.mapper;

import com.project.reportsystem.domain.Request;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.RequestEntity;
import com.project.reportsystem.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public RequestEntity mapRequestToRequestEntity(Request request) {
        return RequestEntity.builder()
                .id(request.getId())
                .entityUser(UserEntity.builder().id(request.getUser().getId()).build())
                .reason(request.getReason())
                .build();
    }

    public Request mapRequestEntityToRequest(RequestEntity entity) {
        return Request.builder()
                .id(entity.getId())
                .reason(entity.getReason())
                .user(User.builder().id(entity.getEntityUser().getId()).build())
                .build();
    }
}
