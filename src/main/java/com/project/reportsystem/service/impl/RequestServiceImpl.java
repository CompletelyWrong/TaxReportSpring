package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Request;
import com.project.reportsystem.repository.RequestRepository;
import com.project.reportsystem.service.RequestService;
import com.project.reportsystem.service.mapper.RequestMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper mapper;

    @Override
    public Request createRequest(Request request) {
        if (Objects.isNull(request)) {
            log.warn("Request is null");
            throw new IllegalArgumentException("Request is null");
        }

        requestRepository.save(mapper.mapRequestToRequestEntity(request));

        return request;
    }

    @Override
    public void deleteRequestById(Long requestId) {
        if (Objects.isNull(requestId)) {
            log.warn("Request id is null");
            throw new IllegalArgumentException("Request id is null");
        }

        requestRepository.deleteById(requestId);
    }

    @Override
    public Page<Request> findAll(Pageable pageable) {
        if (Objects.isNull(pageable)) {
            log.warn("Pageable is null");
            throw new IllegalArgumentException("Pageable is null");
        }

        return requestRepository.findAll(pageable)
                .map(mapper::mapRequestEntityToRequest);
    }
}
