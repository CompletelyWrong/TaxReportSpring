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
@Entity
@Table(name = "inspectors")
@NoArgsConstructor
public class InspectorEntity extends AbstractUserEntity {
    @OneToMany(mappedBy = "inspector", cascade = CascadeType.ALL)
    private List<UserEntity> users;

}
