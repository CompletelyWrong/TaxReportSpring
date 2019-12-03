package com.project.reportsystem.repository;

import com.project.reportsystem.entity.ReportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    Page<ReportEntity> findByEntityUserId(Long userId, Pageable pageable);
}
