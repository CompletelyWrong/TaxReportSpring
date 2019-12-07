package com.project.reportsystem.service.impl;

import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.UserEntity;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.repository.InspectorRepository;
import com.project.reportsystem.repository.UserRepository;
import com.project.reportsystem.service.mapper.InspectorMapper;
import com.project.reportsystem.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final InspectorRepository inspectorRepository;
    private final UserRepository userRepository;
    private final InspectorMapper inspectorMapper;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) {
        final Optional<InspectorEntity> entity = inspectorRepository.findByEmail(email);

        if (!entity.isPresent()) {
            final Optional<UserEntity> userEntity = userRepository.findByEmail(email);
            if (!userEntity.isPresent()) {

                throw new EntityNotFoundException("No user found with such email");
            }
            return userMapper.mapUserEntityToUser(userEntity.get());
        }
        return inspectorMapper.mapInspectorEntityToInspector(entity.get());
    }
}
