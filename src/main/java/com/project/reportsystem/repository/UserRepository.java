package com.project.reportsystem.repository;

import com.project.reportsystem.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findAll(Pageable pageable);

    Page<UserEntity> findAllByInspectorId(Long inspectorId, Pageable pageable);

    List<UserEntity> findAllByInspectorId(Long inspectorId);
}
