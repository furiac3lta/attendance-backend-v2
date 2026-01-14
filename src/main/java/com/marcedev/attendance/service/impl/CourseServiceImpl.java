package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.EnrollmentRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    @Override
    public List<Course> findAll() {
        return courseRepository.findByActiveTrue();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public Course saveCourseWithAuthenticatedInstructor(Course course) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User instructor = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("Instructor no encontrado"));

        if (instructor.getOrganization() == null) {
            throw new IllegalStateException("El instructor no pertenece a ninguna organización");
        }

        course.setInstructor(instructor);
        course.setOrganization(instructor.getOrganization());

        return courseRepository.save(course);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, Course updatedCourse) {
        return courseRepository.findByIdAndActiveTrue(id)
                .map(existing -> {
                    existing.setName(updatedCourse.getName());
                    existing.setDescription(updatedCourse.getDescription());
                    existing.setUniversityProgram(updatedCourse.getUniversityProgram());
                    return courseRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
    }

    @Override
    public void deactivateCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        course.setActive(false);
        courseRepository.save(course);
    }

    @Override
    public void activateCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));
        course.setActive(true);
        courseRepository.save(course);
    }

    /**
     * 🔹 Inscribir un alumno al curso
     */
    @Override
    public Course addStudentToCourse(Long courseId, Long userId) {
        // 🧠 Obtener curso
        Course course = courseRepository.findByIdAndActiveTrue(courseId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Curso no encontrado con ID: " + courseId));

        // 🧠 Obtener alumno
        User student = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Usuario no encontrado con ID: " + userId));

        // 🧱 Validar organización
        if (course.getOrganization() == null || student.getOrganization() == null) {
            throw new IllegalStateException("🚫 Curso o alumno sin organización asociada.");
        }

        if (!course.getOrganization().getId().equals(student.getOrganization().getId())) {
            throw new IllegalStateException("🚫 El alumno pertenece a otra organización.");
        }

        // 🔁 Evitar duplicados
        if (course.getStudents() != null && course.getStudents().stream()
                .anyMatch(u -> u.getId().equals(student.getId()))) {
            System.out.println("⚠️ El alumno ya estaba inscripto en el curso.");
            return course;
        }

        // 🧩 Agregar alumno al curso
        course.getStudents().add(student);

        // 💾 Guardar
        Course saved = courseRepository.save(course);
        System.out.println("✅ Alumno agregado correctamente: " + student.getEmail());

        return saved;
    }

    /**
     * 🔹 Remover un alumno del curso
     */
    @Override
    public Course removeStudentFromCourse(Long courseId, Long userId) {
        Course course = courseRepository.findByIdAndActiveTrue(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        User student = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (course.getStudents().contains(student)) {
            course.getStudents().remove(student);
            student.getCourses().remove(course);
            userRepository.save(student);
            courseRepository.save(course);
        }

        return course;
    }

    @Override
    public List<Course> findMyCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // ✅ SUPER_ADMIN ve todos los cursos, sin requerir organización
        if (user.getRole() == Rol.SUPER_ADMIN) {
            return courseRepository.findByActiveTrue();
        }

        // ⚠️ Si no tiene organización, no puede ver cursos
        Organization org = user.getOrganization();
        if (org == null) {
            throw new IllegalStateException("El usuario no pertenece a ninguna organización");
        }

        // ✅ Si es admin → ve todos los cursos de su organización
        if (user.getRole() == Rol.ADMIN) {
            return courseRepository.findByOrganizationAndActiveTrue(org);
        }

        // ✅ Si es instructor → ve sus propios cursos
        if (user.getRole() == Rol.INSTRUCTOR) {
            return courseRepository.findByOrganizationAndInstructorAndActiveTrue(org, user);
        }

        // ✅ Si es alumno → devuelve sus cursos inscritos
        if (user.getRole() == Rol.USER) {
            return user.getCourses().stream()
                    .filter(Course::isActive)
                    .toList();
        }

        return List.of();
    }

    public void assignInstructor(Long courseId, Long instructorId) {

        Course course = courseRepository.findByIdAndActiveTrue(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        User instructor = userRepository.findByIdAndActiveTrue(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado"));

        // 🔐 VALIDACIÓN CLAVE
        if (instructor.getRole() != Rol.INSTRUCTOR) {
            throw new IllegalStateException(
                    "Solo usuarios con rol INSTRUCTOR pueden ser asignados a un curso"
            );
        }

        // (opcional pero sano) validar misma organización
        if (course.getOrganization() != null &&
                instructor.getOrganization() != null &&
                !course.getOrganization().getId().equals(instructor.getOrganization().getId())) {

            throw new IllegalStateException("El instructor pertenece a otra organización");
        }

        course.setInstructor(instructor);
        courseRepository.save(course);
    }
    @Override
    public List<DebtorDTO> getDebtorsByOrganization(
            Long organizationId,
            int month,
            int year
    ) {
        return enrollmentRepository.findDebtorsByOrganization(
                organizationId,
                month,
                year
        );
    }

}
