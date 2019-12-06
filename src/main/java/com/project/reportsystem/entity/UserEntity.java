package com.project.reportsystem.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "users")
@Entity
@NoArgsConstructor
public class UserEntity extends AbstractUserEntity {
    @Column(name = "inn_code", length = 50, nullable = false)
    private Integer identificationCode;

    @OneToMany(mappedBy = "entityUser", cascade = CascadeType.ALL)
    private List<ReportEntity> reports;

    @OneToMany(mappedBy = "entityUser", cascade = CascadeType.ALL)
    private List<RequestEntity> request;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private InspectorEntity inspector;

}
