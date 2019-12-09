package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.exception.AlreadyExistUserException;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.exception.InspectorNotFoundException;
import com.project.reportsystem.repository.InspectorRepository;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.mapper.InspectorMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class InspectorServiceImpl implements InspectorService {
    private final InspectorRepository inspectorRepository;
    private final BCryptPasswordEncoder encoder;
    private final InspectorMapper mapper;

    @Override
    public Inspector createInspector(Inspector inspector) {
        if (Objects.isNull(inspector)) {
            log.warn("Inspector is null");
            throw new IllegalArgumentException("Inspector is null");
        }

        if (inspectorRepository.findByEmail(inspector.getEmail()).isPresent()) {
            log.warn("Inspector is already registered by this e-mail");
            throw new AlreadyExistUserException("Inspector is already registered by this e-mail");
        }

        String encoded = encoder.encode(inspector.getPassword());
        inspector.setPassword(encoded);
        inspectorRepository.save(mapper.mapInspectorToInspectorEntity(inspector));

        return inspector;
    }

    @Override
    public Inspector updateInfo(Inspector inspector) {
        if (Objects.isNull(inspector)) {
            log.warn("Inspector is null");
            throw new IllegalArgumentException("Inspector is null");
        }

        inspectorRepository.findById(inspector.getId()).orElseThrow(() -> {
            log.warn("There is no inspector with this such id");
            return new EntityNotFoundException("There is no inspector with this such id");
        });

        String encoded = encoder.encode(inspector.getPassword());
        inspector.setPassword(encoded);
        inspectorRepository.save(mapper.mapInspectorToInspectorEntity(inspector));

        return inspector;
    }

    @Override
    public Inspector findByUserId(Long userId) {
        if (Objects.isNull(userId)) {
            log.warn("Inspector is null");
            throw new IllegalArgumentException("Inspector is null");
        }

        return inspectorRepository.findByUserId(userId)
                .map(mapper::mapInspectorEntityToInspector)
                .orElseThrow(() -> {
                    log.warn("There is no inspector by this user id");
                    return new EntityNotFoundException("There is no inspector by this user id");
                });
    }

    @Override
    public Page<Inspector> findAll(Pageable pageable) {
        if (Objects.isNull(pageable)) {
            log.warn("Pageable is null");
            throw new IllegalArgumentException("Pageable is null");
        }

        return inspectorRepository.findAllByRole(pageable, Role.INSPECTOR)
                .map(mapper::mapInspectorEntityToInspector);
    }

    @Override
    public Inspector findWithLessUsers() {
        return inspectorRepository.findWithLessUsers()
                .map(mapper::mapInspectorEntityToInspector)
                .orElseThrow(() -> {
                    log.warn("There is no available inspectors in system");
                    return new InspectorNotFoundException("There is no available inspectors in system");
                });
    }

    @Override
    public Inspector findWithLessUsersExceptThisId(Long inspectorId) {
        if (Objects.isNull(inspectorId)) {
            log.warn("Inspector id is null");
            throw new IllegalArgumentException("Inspector id is null");
        }

        return inspectorRepository.findWithLessUsersExceptThisId(inspectorId)
                .map(mapper::mapInspectorEntityToInspector)
                .orElseThrow(() -> {
                    log.warn("There is no available inspectors in system");
                    return new InspectorNotFoundException("There is no available inspectors in system");
                });
    }

}
