package com.marcedev.attendance.repository;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Payment;
import com.marcedev.attendance.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByStudentIdAndCourseIdAndMonthAndYearAndStatus(
            Long studentId,
            Long courseId,
            int month,
            int year,
            PaymentStatus status
    );

    boolean existsByStudentIdAndMonthAndYear(
            Long studentId,
            int month,
            int year
    );

    @Query("""
        SELECT p FROM Payment p
        WHERE p.course.id = :courseId
          AND p.month = :month
          AND p.year = :year
        ORDER BY p.student.fullName
    """)
    List<Payment> findByCourseMonthYear(
            @Param("courseId") Long courseId,
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
        SELECT p FROM Payment p
        WHERE p.student.id = :studentId
        ORDER BY p.year DESC, p.month DESC
    """)
    List<Payment> findByStudent(@Param("studentId") Long studentId);

    @Query("""
    select p
    from Payment p
    where p.student.id = :studentId
      and p.course.id = :courseId
      and p.status = 'PAID'
    order by p.year desc, p.month desc
""")
    Optional<Payment> findLastPayment(Long studentId, Long courseId);

    Optional<Payment> findTopByStudentIdAndCourseIdOrderByPaidAtDesc(
            Long studentId,
            Long courseId
    );
    @Query("""
    SELECT COUNT(DISTINCT p.student.id)
    FROM Payment p
    WHERE p.month = :month
      AND p.year = :year
      AND p.status = 'PAID'
""")
    long countPaidStudents(
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.month = :month
      AND p.year = :year
      AND p.status = 'PAID'
""")
    java.math.BigDecimal sumPaidAmount(
            @Param("month") int month,
            @Param("year") int year
    );

    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Payment p
    WHERE p.course.organization.id = :organizationId
      AND p.status = 'PAID'
""")
    BigDecimal sumPaidAmountByOrganization(
            @Param("organizationId") Long organizationId
    );
    @Query("""
    SELECT COUNT(DISTINCT p.student.id)
    FROM Payment p
    WHERE p.course.organization.id = :orgId
      AND p.year = :year
      AND p.month = :month
      AND p.status = 'PAID'
""")
    long countPaidStudentsByOrganization(
            @Param("orgId") Long orgId,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("""
    SELECT DISTINCT p.student.id, p.course.id
    FROM Payment p
    WHERE p.status = 'PAID'
      AND p.month = :month
      AND p.year = :year
      AND p.course.organization.id = :orgId
""")
    List<Object[]> findPaidStudents(
            @Param("orgId") Long orgId,
            @Param("month") int month,
            @Param("year") int year
    );



        boolean existsByStudentIdAndCourseIdAndStatusAndMonthAndYear(
                Long studentId,
                Long courseId,
                String status,
                int month,
                int year
        );

    @Query("""
SELECT DISTINCT new com.marcedev.attendance.dto.DebtorDTO(
    u.id,
    u.fullName,
    c.name
)
FROM Course c
JOIN c.students u
WHERE c.organization.id = :orgId
  AND u.role = 'USER'
  AND NOT EXISTS (
      SELECT 1
      FROM Payment p
      WHERE p.student = u
        AND p.course = c
        AND p.status = 'PAID'
        AND p.month = :month
        AND p.year = :year
  )
""")
    List<DebtorDTO> findDebtorsByOrganization(
            @Param("orgId") Long orgId,
            @Param("month") int month,
            @Param("year") int year
    );

}
