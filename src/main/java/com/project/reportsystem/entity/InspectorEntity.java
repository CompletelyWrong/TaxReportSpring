package com.project.reportsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "inspectors")
@NoArgsConstructor(force = true)
public class InspectorEntity extends AbstractUserEntity {

    @OneToMany(mappedBy = "inspector", cascade = CascadeType.ALL)
    private final List<UserEntity> users;

}
