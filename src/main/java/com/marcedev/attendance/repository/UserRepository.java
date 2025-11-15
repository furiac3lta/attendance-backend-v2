package com.marcedev.attendance.repository;

import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"courses"})
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"courses"})
    Page<User> findByOrganizationId(Long organizationId, Pageable pageable);

    @EntityGraph(attributePaths = {"courses"})
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    List<User> findByRole(Rol role);

    List<User> findByOrganizationId(Long orgId);

    Optional<User> findByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u JOIN u.courses c WHERE c.id IN :courseIds")
    List<User> findDistinctByCoursesIdIn(@Param("courseIds") List<Long> courseIds);

    List<User> findByRoleAndOrganizationId(Rol role, Long organizationId);

    // ======================================================
    // ✔ FILTRO AVANZADO (PAGINADO) — ENUM limpio
    // ======================================================
    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN u.organization o
        LEFT JOIN u.courses c
        WHERE 
            (:search IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                             OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:role IS NULL OR u.role = :role)
        AND (:orgId IS NULL OR o.id = :orgId)
        AND (:courseId IS NULL OR c.id = :courseId)
        """)
    Page<User> filterUsers(
            @Param("search") String search,
            @Param("role") Rol role,
            @Param("orgId") Long orgId,
            @Param("courseId") Long courseId,
            Pageable pageable
    );
}
