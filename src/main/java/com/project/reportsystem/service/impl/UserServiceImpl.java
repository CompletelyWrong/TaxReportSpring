package com.project.reportsystem.service.impl;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.UserEntity;
import com.project.reportsystem.exception.AlreadyExistUserException;
import com.project.reportsystem.exception.EntityNotFoundException;
import com.project.reportsystem.repository.UserRepository;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.UserService;
import com.project.reportsystem.service.mapper.UserMapper;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final InspectorService inspectorService;
    private final BCryptPasswordEncoder encoder;
    private final UserMapper mapper;

    @Override
    public User register(User user) {
        if (Objects.isNull(user)) {
            log.warn("User is null");
            throw new IllegalArgumentException("User is null");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("User is already registered by this e-mail");
            throw new AlreadyExistUserException("User is already registered by this e-mail");
        }

        String encoded = encoder.encode(user.getPassword());
        user.setPassword(encoded);
        Inspector inspector = inspectorService.findWithLessUsers();
        userRepository.save(mapper.mapUserToUserEntityWithInspector(user, inspector));

        return user;
    }

    @Override
    public User findById(Long userId) {
        if (Objects.isNull(userId)) {
            log.warn("User id is null");
            throw new IllegalArgumentException("User id is null");
        }

        return userRepository.findById(userId)
                .map(mapper::mapUserEntityToUser)
                .orElseThrow(() -> {
                    log.warn("There is no user with this such id");
                    return new EntityNotFoundException("There is no user with this such id");
                });
    }

    @Override
    public User login(String email, String password) {
        if (Objects.isNull(email) || Objects.isNull(password)) {
            log.warn("Email / password id is null");
            throw new IllegalArgumentException("Email / password id is null");
        }

        UserEntity entity = userRepository.findByEmail(email).orElseThrow(() -> {
            log.warn("There is no user with this email");
            return new EntityNotFoundException("There is no user with this email");
        });

        if (encoder.matches(password, entity.getPassword())) {
            return mapper.mapUserEntityToUser(entity);
        }

        log.warn("Incorrect password");
        throw new EntityNotFoundException("Incorrect password");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        if (Objects.isNull(pageable)) {
            log.warn("Pageable is null");
            throw new IllegalArgumentException("Pageable is null");
        }

        return userRepository.findAll(pageable).map(mapper::mapUserEntityToUser);
    }

    @Override
    public User updateInfo(User user) {
        validate(user);

        String encoded = encoder.encode(user.getPassword());
        user.setPassword(encoded);
        userRepository.save(mapper.mapUserToUserEntity(user));

        return user;
    }

    @Override
    public User changeInspectorForUser(User user) {
        final UserEntity entity = validate(user);

        Inspector inspector = inspectorService.findWithLessUsersExceptThisId(entity.getInspector().getId());
        userRepository.save(mapper.mapUserToUserEntityWithInspector(user, inspector));

        return user;
    }

    @Override
    public Page<User> findAllByInspectorId(Long inspectorId, Pageable pageable) {
        if (Objects.isNull(inspectorId) || Objects.isNull(pageable)) {
            log.warn("Pageable/Inspector id is null");
            throw new IllegalArgumentException("Pageable/Inspector id is null");
        }

        return userRepository.findAllByInspectorId(inspectorId, pageable)
                .map(mapper::mapUserEntityToUser);
    }

    private UserEntity validate(User user) {
        if (Objects.isNull(user)) {
            log.warn("User is null");
            throw new IllegalArgumentException("User is null");
        }

        return userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    log.warn("There is no user with this such id");
                    return new EntityNotFoundException("There is no user with this such id");
                });
    }
}
