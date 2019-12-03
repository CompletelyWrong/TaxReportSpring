package com.project.reportsystem.repository;

import com.project.reportsystem.entity.ActionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<ActionEntity, Long> {
    Page<ActionEntity> findAllByReportEntityId(Long reportId, Pageable pageable);
}
