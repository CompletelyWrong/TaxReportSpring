package com.project.reportsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Table(name = "reports")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity entityUser;

    @Column(name = "file_link", length = 500, nullable = false)
    private String fileLink;

    @Column(name = "date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @OneToMany(mappedBy = "reportEntity", cascade = CascadeType.ALL)
    private List<ActionEntity> actions;
}
