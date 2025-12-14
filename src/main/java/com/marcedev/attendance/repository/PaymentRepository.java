package com.marcedev.attendance.repository;

import com.marcedev.attendance.entities.Payment;
import com.marcedev.attendance.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
