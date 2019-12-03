package com.project.reportsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "actions")
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inspector_id", referencedColumnName = "id")
    private final InspectorEntity inspectorEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private final ReportEntity reportEntity;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime dateTime;

    @Column(name = "name", length = 50, nullable = false)
    private final String message;

    @Enumerated(EnumType.STRING)
    private final ActionType action;
}
