package com.project.reportsystem.service;

import com.project.reportsystem.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User register(User user);

    User findById(Long userId);

    User login(String login, String password);

    Page<User> findAll(Pageable pageable);

    User updateInfo(User user);

    User changeInspectorForUser(User user);

    Page<User> findAllByInspectorId(Long inspectorId, Pageable pageable);
}
