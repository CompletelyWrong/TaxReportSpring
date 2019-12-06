package com.project.reportsystem.service;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InspectorService {
    Inspector createInspector(Inspector inspector);

    Inspector login(String login, String password);

    Inspector updateInfo(Inspector inspector);

    Inspector findByUserId(Long userId);

    Page<Inspector> findAll(Pageable pageable);

    Inspector findWithLessUsers();

    Inspector findWithLessUsersExceptThisId(Long inspectorId);
}
