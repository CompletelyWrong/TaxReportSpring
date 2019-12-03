package com.project.reportsystem.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "request")
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private final UserEntity entityUser;

    @Column(name = "message", length = 500, nullable = false)
    private final String reason;
}
