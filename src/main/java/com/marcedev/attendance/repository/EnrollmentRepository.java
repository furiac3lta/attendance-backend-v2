package com.marcedev.attendance.repository;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByCourseIdAndActiveTrue(Long courseId);

    List<Enrollment> findByUserIdAndActiveTrue(Long userId);

    @Query("""
        SELECT COUNT(e)
        FROM Enrollment e
        WHERE e.course.organization.id = :organizationId
        AND e.active = true
    """)
    long countActiveByOrganization(Long organizationId);

    @Query("""
        SELECT COUNT(e)
        FROM Enrollment e
        WHERE e.course.id = :courseId
        AND e.active = true
    """)
    long countActiveByCourse(Long courseId);

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

    @Query("""
    SELECT DISTINCT new com.marcedev.attendance.dto.DebtorDTO(
        u.id,
        u.fullName,
        c.name
    )
    FROM Enrollment e
    JOIN e.user u
    JOIN e.course c
    WHERE c.organization.id = :orgId
      AND e.active = true
      AND NOT EXISTS (
          SELECT 1
          FROM Payment p
          WHERE p.student = u
            AND p.course = c
            AND p.status = com.marcedev.attendance.enums.PaymentStatus.PAID
      )
""")
    List<DebtorDTO> findDebtorsByOrganization(@Param("orgId") Long orgId);

}
