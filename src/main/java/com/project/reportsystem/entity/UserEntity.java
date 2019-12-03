package com.project.reportsystem.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "users")
@Entity
@NoArgsConstructor(force = true)
public class UserEntity extends AbstractUserEntity {
    @Column(name = "inn_code", length = 50, nullable = false)
    private final Integer identificationCode;

    @OneToMany(mappedBy = "entityUser", cascade = CascadeType.ALL)
//    @Setter(AccessLevel.PUBLIC)
    private List<ReportEntity> reports;

    @OneToMany(mappedBy = "entityUser", cascade = CascadeType.ALL)
//    @Setter(AccessLevel.PUBLIC)
    private List<RequestEntity> request;

    @ManyToOne
    @JoinColumn(name = "inspector_id")
    private InspectorEntity inspector;

}
