package com.project.reportsystem.service;

import com.project.reportsystem.domain.Inspector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InspectorService {
    Inspector createInspector(Inspector inspector);

    Inspector login(String login, String password);

    Inspector updateInfo(Inspector inspector);

    Page<Inspector> findAll(Pageable pageable);

    Inspector findWithLessUsers();

    Inspector findWithLessUsersExceptThisId(Long inspectorId);
}
