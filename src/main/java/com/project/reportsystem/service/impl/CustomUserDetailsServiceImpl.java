package com.project.reportsystem.service.impl;

import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.UserEntity;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.repository.InspectorRepository;
import com.project.reportsystem.repository.UserRepository;
import com.project.reportsystem.service.mapper.InspectorMapper;
import com.project.reportsystem.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Log4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final InspectorRepository inspectorRepository;
    private final UserRepository userRepository;
    private final InspectorMapper inspectorMapper;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        if (Objects.isNull(email)) {
            log.warn("Email id is null");
            throw new IllegalArgumentException("Email id is null");
        }

        final Optional<InspectorEntity> entity = inspectorRepository.findByEmail(email);

        if (!entity.isPresent()) {
            final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
            if (!userEntity.isPresent()) {
                log.warn("No user found with such email");
                throw new EntityNotFoundException("No user found with such email");
            }
            return userMapper.mapUserEntityToUser(userEntity.get());
        }
        return inspectorMapper.mapInspectorEntityToInspector(entity.get());
    }
}
