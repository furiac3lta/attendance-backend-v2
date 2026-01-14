package com.marcedev.attendance.repository;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructorId(Long instructorId);

    List<Course> findByInstructorIdAndActiveTrue(Long instructorId);

    List<Course> findByInstructorIdAndActiveFalse(Long instructorId);

    // 🔹 Buscar todos los cursos de una organización
    List<Course> findByOrganization(Organization organization);

    // 🔹 Buscar todos los cursos por instructor dentro de una organización
    List<Course> findByOrganizationAndInstructor(Organization organization, User instructor);

    List<Course> findByOrganizationId(Long organizationId);

    List<Course> findByActiveTrue();

    List<Course> findByActiveFalse();

    List<Course> findByOrganizationAndActiveTrue(Organization organization);

    List<Course> findByOrganizationAndActiveFalse(Organization organization);

    List<Course> findByOrganizationAndInstructorAndActiveTrue(Organization organization, User instructor);

    List<Course> findByOrganizationAndInstructorAndActiveFalse(Organization organization, User instructor);

    List<Course> findByOrganizationIdAndActiveTrue(Long organizationId);

    List<Course> findByOrganizationIdAndActiveFalse(Long organizationId);

    Optional<Course> findByIdAndActiveTrue(Long id);

    Optional<Course> findByNameIgnoreCaseAndActiveTrue(String name);

    Optional<Course> findByNameIgnoreCaseAndOrganizationIdAndActiveTrue(String name, Long organizationId);

    @Query("""
    SELECT u
    FROM Course c
    JOIN c.students u
    WHERE c.id = :courseId
""")
    List<User> findStudentsByCourseId(Long courseId);
    @Query("""
SELECT DISTINCT new com.marcedev.attendance.dto.DebtorDTO(
    u.id,
    u.fullName,
    c.name
)
FROM Course c
JOIN c.students u
WHERE c.organization.id = :orgId
AND NOT EXISTS (
    SELECT 1
    FROM Payment p
    WHERE p.student = u
      AND p.course = c
      AND p.status = com.marcedev.attendance.enums.PaymentStatus.PAID
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
