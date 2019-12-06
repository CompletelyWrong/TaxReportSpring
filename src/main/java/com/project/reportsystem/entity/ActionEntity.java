package com.project.reportsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@Table(name = "actions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inspector_id", referencedColumnName = "id")
    private InspectorEntity inspectorEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportId", referencedColumnName = "id")
    private ReportEntity reportEntity;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @Column(name = "name", length = 50, nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private ActionType action;
}
