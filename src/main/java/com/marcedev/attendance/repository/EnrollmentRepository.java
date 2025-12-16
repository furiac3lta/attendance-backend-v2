package com.marcedev.attendance.repository;

import com.marcedev.attendance.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}
