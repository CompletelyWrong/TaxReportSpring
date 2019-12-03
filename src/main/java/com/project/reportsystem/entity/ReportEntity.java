package com.project.reportsystem.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Table(name = "reports")
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private final UserEntity entityUser;

    @Column(name = "file_link", length = 500, nullable = false)
    private final String fileLink;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private final ReportStatus status;

    @OneToMany(mappedBy = "reportEntity", cascade = CascadeType.ALL)
    private List<ActionEntity> actions;
}
