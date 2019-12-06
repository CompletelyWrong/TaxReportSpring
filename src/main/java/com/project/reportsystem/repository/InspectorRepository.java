package com.project.reportsystem.repository;

import com.project.reportsystem.entity.InspectorEntity;
import com.project.reportsystem.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InspectorRepository extends JpaRepository<InspectorEntity, Long> {
    String FIND_INSPECTOR_WITH_LESS_USERS_EXCEPT_ID_QUERY = "SELECT inspectors.id, inspectors.email, inspectors.name, inspectors.password, inspectors.patronymic," +
            " inspectors.role, inspectors.surname FROM spring.inspectors left join spring.users on users.inspector_id=inspectors.id where inspectors.role<> 'ADMIN'" +
            " and inspectors.id <> :id  GROUP BY inspectors.id ORDER BY COUNT(*) asc LIMIT 1";

    String FIND_INSPECTOR_WITH_LESS_USERS_QUERY = "SELECT inspectors.id, inspectors.email, inspectors.name, inspectors.password, inspectors.patronymic, inspectors.role," +
            " inspectors.surname FROM spring.inspectors left join spring.users on users.inspector_id=inspectors.id where inspectors.role<>" +
            " 'ADMIN' GROUP BY inspectors.id ORDER BY COUNT(*) asc LIMIT 1";

    String FIND_BY_USER_ID_QUERY = "SELECT inspectors.id, inspectors.email, inspectors.name, inspectors.password, inspectors.patronymic, inspectors.role, inspectors.surname" +
            " FROM spring.users join spring.inspectors on users.inspector_id = inspectors.id where users.id = :id";

    Optional<InspectorEntity> findByEmail(String email);

    @Query(value = FIND_BY_USER_ID_QUERY, nativeQuery = true)
    Optional<InspectorEntity> findByUserId(@Param("id") Long userId);

    Page<InspectorEntity> findAllByRole(Pageable pageable, Role role);

    @Query(value = FIND_INSPECTOR_WITH_LESS_USERS_QUERY, nativeQuery = true)
    Optional<InspectorEntity> findWithLessUsers();

    @Query(value = FIND_INSPECTOR_WITH_LESS_USERS_EXCEPT_ID_QUERY, nativeQuery = true)
    Optional<InspectorEntity> findWithLessUsersExceptThisId(@Param("id") Long inspectorId);
}
