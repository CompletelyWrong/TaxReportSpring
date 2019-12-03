package com.project.reportsystem.service;

import com.project.reportsystem.domain.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestService {
    Request createRequest(Request request);

    void deleteRequestById(Long requestId);

    Page<Request> findAll(Pageable pageable);
}
