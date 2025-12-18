package com.marcedev.attendance.repository;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Enrollment;
import com.marcedev.attendance.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Enrollment, Long> {

    // ================= ALUMNOS =================

    @Query(value = """
        SELECT COUNT(*)
        FROM enrollments e
        JOIN courses c ON e.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND e.active = true
        """, nativeQuery = true)
    long countActiveStudents(@Param("organizationId") Long organizationId);

    @Query(value = """
        SELECT c.name, COUNT(*)
        FROM enrollments e
        JOIN courses c ON e.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND e.active = true
        GROUP BY c.name
        """, nativeQuery = true)
    List<Object[]> countStudentsByCourse(@Param("organizationId") Long organizationId);

    // ================= PAGOS =================

    @Query(value = """
        SELECT COUNT(DISTINCT p.student_id)
        FROM payments p
        JOIN courses c ON p.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND p.year = :year
          AND p.month = :month
          AND p.status = 'PAID'
        """, nativeQuery = true)
    long countPaidStudents(
            @Param("organizationId") Long organizationId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query(value = """
        SELECT COUNT(DISTINCT e.user_id)
        FROM enrollments e
        JOIN courses c ON e.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND e.active = true
          AND e.user_id NOT IN (
              SELECT p.student_id
              FROM payments p
              WHERE p.year = :year
                AND p.month = :month
                AND p.status = 'PAID'
          )
        """, nativeQuery = true)
    long countDebtStudents(
            @Param("organizationId") Long organizationId,
            @Param("year") int year,
            @Param("month") int month
    );

    // ================= RECAUDACIÓN =================

    @Query(value = """
        SELECT COALESCE(SUM(p.amount), 0)
        FROM payments p
        JOIN courses c ON p.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND p.year = :year
          AND p.month = :month
          AND p.status = 'PAID'
        """, nativeQuery = true)
    BigDecimal totalRevenue(
            @Param("organizationId") Long organizationId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query(value = """
        SELECT c.name, COALESCE(SUM(p.amount), 0)
        FROM payments p
        JOIN courses c ON p.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND p.year = :year
          AND p.month = :month
          AND p.status = 'PAID'
        GROUP BY c.name
        """, nativeQuery = true)
    List<Object[]> revenueByCourse(
            @Param("organizationId") Long organizationId,
            @Param("year") int year,
            @Param("month") int month
    );

    // ================= MÉTODOS DE PAGO =================

    @Query(value = """
        SELECT COALESCE(SUM(p.amount), 0)
        FROM payments p
        JOIN courses c ON p.course_id = c.id
        WHERE c.organization_id = :organizationId
          AND p.year = :year
          AND p.month = :month
          AND p.status = 'PAID'
          AND p.method = :method
        """, nativeQuery = true)
    BigDecimal revenueByPaymentMethod(
            @Param("organizationId") Long organizationId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("method") PaymentMethod method
    );
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
