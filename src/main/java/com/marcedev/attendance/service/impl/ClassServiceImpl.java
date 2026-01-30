package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.ClassDetailsDTO;
import com.marcedev.attendance.entities.ClassSession;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.repository.ClassSessionRepository;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private static final ZoneId APP_ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    private final ClassSessionRepository classSessionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public ClassSession create(ClassSession session) {
        if (session.getCourse() == null || session.getCourse().getId() == null) {
            throw new RuntimeException("El curso es requerido");
        }

        var course = courseRepository.findByIdAndActiveTrue(session.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        var instructor = (course.getInstructor() != null)
                ? course.getInstructor()
                : getAuthenticatedUser();

        session.setCourse(course);
        session.setInstructor(instructor);
        session.setOrganization(course.getOrganization());

        return classSessionRepository.save(session);
    }

    @Override
    public ClassSession findById(Long id) {
        return classSessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<ClassSession> findByCourseId(Long courseId) {
        return classSessionRepository.findByCourseIdAndActiveTrue(courseId);
    }

    @Override
    public Optional<ClassSession> findByCourseIdAndDate(Long courseId, LocalDate date) {
        return classSessionRepository.findByCourseIdAndDateAndActiveTrue(courseId, date);
    }

    @Override
    public ClassSession getOrCreateTodaySession(Long courseId) {

        LocalDate today = LocalDate.now(APP_ZONE);

        Course course = courseRepository.findByIdAndActiveTrue(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado."));

        return classSessionRepository.findByCourseIdAndDateAndActiveTrue(courseId, today)
                .orElseGet(() -> {

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String email = auth.getName();
                    User currentUser = userRepository.findByEmailAndActiveTrue(email).orElse(null);

                    Organization org = course.getOrganization() != null
                            ? course.getOrganization()
                            : currentUser != null ? currentUser.getOrganization() : null;

                    if (org == null) {
                        throw new RuntimeException("⚠️ No se puede determinar la organización para la clase.");
                    }

                    ClassSession newSession = new ClassSession();
                    newSession.setCourse(course);
                    newSession.setDate(today);
                    newSession.setName(course.getName() + " – " + today);
                    newSession.setInstructor(currentUser);
                    newSession.setOrganization(org);

                    return classSessionRepository.save(newSession);
                });
    }

    @Override
    public ClassDetailsDTO getClassDetails(Long classId) {

        ClassSession classSession = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Course course = classSession.getCourse();
        String courseName = course != null ? course.getName() : "Sin curso";

        return new ClassDetailsDTO(
                classSession.getId(),
                classSession.getName(), // esto va en className
                classSession.getDate(),
                courseName,
                course.getId(),
                classSession.getObservations(),
                classSession.isQrEnabled()
        );


    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findByIdAndActiveTrue(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con id: " + courseId));
    }

    @Override
    public List<ClassSession> findByOrganization(Long organizationId) {
        return classSessionRepository.findAll().stream()
                .filter(c -> c.isActive()
                        && c.getOrganization() != null
                        && c.getOrganization().getId().equals(organizationId))
                .toList();
    }

    @Override
    public List<ClassSession> findByCourseIdInactive(Long courseId) {
        return classSessionRepository.findByCourseIdAndActiveFalse(courseId);
    }

    @Override
    public void deactivateClass(Long id) {
        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        session.setActive(false);
        session.setQrEnabled(false);
        session.setQrToken(null);
        session.setQrExpiresAt(null);
        classSessionRepository.save(session);
    }

    @Override
    public void activateClass(Long id) {
        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        session.setActive(true);
        classSessionRepository.save(session);
    }
    // ================== AUTH ==================
    private User getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        return userRepository.findByEmailAndActiveTrue(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
