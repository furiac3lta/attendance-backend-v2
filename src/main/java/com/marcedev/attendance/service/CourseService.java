package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> findAll();
    Optional<Course> findById(Long id);

    // 🔹 Crea un curso asignando automáticamente el instructor autenticado
    Course saveCourseWithAuthenticatedInstructor(Course course);

    Course save(Course course);
    Course update(Long id, Course course);
    void deactivateCourse(Long id);
    void activateCourse(Long id);

    // 🔹 Inscribir o remover alumnos
    Course addStudentToCourse(Long courseId, Long userId);
    Course removeStudentFromCourse(Long courseId, Long userId);

    // ✅ Nuevo método para obtener los cursos del usuario autenticado
    List<Course> findMyCourses();

    void assignInstructor(Long courseId, Long instructorId);

    // 👇 AGREGAR
    List<DebtorDTO> getDebtorsByOrganization(
            Long organizationId,
            int month,
            int year
    );
}
