package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.*;
import com.marcedev.attendance.entities.*;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.mapper.AttendanceMapper;
import com.marcedev.attendance.repository.*;
import com.marcedev.attendance.service.AttendanceService;
import com.marcedev.attendance.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ClassSessionRepository classSessionRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper attendanceMapper;
    private final CourseRepository courseRepository;
    private final PaymentService paymentService;

    // =========================================================
    // 🔐 AUTH
    // =========================================================

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // =========================================================
    // CRUD
    // =========================================================

    @Override
    @Transactional
    public AttendanceDTO save(AttendanceDTO dto) {

        if (dto.getClassSessionId() == null || dto.getStudentId() == null) {
            throw new RuntimeException("classSessionId y studentId son obligatorios");
        }

        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() == Rol.USER) {
            throw new RuntimeException("No autorizado");
        }

        Attendance entity = attendanceRepository
                .findByStudentIdAndClassSessionId(dto.getStudentId(), dto.getClassSessionId())
                .orElseGet(() -> attendanceMapper.toEntity(dto));

        ClassSession session = classSessionRepository.findById(dto.getClassSessionId())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        boolean upToDate = paymentService.isStudentUpToDate(
                student.getId(),
                session.getCourse().getId()
        );

        entity.setClassSession(session);
        entity.setStudent(student);
        entity.setCourse(session.getCourse());
        entity.setOrganization(session.getOrganization());
        entity.setAttended(dto.isAttended());
        entity.setHasDebt(!upToDate);
        entity.setTakenBy(currentUser);
        entity.setTakenAt(LocalDateTime.now());

        return attendanceMapper.toDTO(attendanceRepository.save(entity));
    }

    @Override
    public List<AttendanceDTO> findAll() {
        User currentUser = getAuthenticatedUser();

        List<Attendance> list = switch (currentUser.getRole()) {
            case SUPER_ADMIN -> attendanceRepository.findAll();
            case ADMIN, INSTRUCTOR ->
                    attendanceRepository.findByOrganizationId(
                            currentUser.getOrganization().getId()
                    );
            default -> throw new RuntimeException("No autorizado");
        };

        return list.stream()
                .map(attendanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDTO findById(Long id) {
        return attendanceRepository.findById(id)
                .map(attendanceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    @Override
    public List<AttendanceDTO> findByClassId(Long classId) {
        return attendanceRepository.findByClassSessionId(classId)
                .stream()
                .map(attendanceMapper::toDTO)
                .toList();
    }

    @Override
    public List<AttendanceDTO> findByCourseId(Long courseId) {
        return attendanceRepository.findByCourseId(courseId)
                .stream()
                .map(attendanceMapper::toDTO)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() == Rol.USER) {
            throw new RuntimeException("No autorizado");
        }

        attendanceRepository.deleteById(id);
    }

    // =========================================================
    // ✅ REGISTRO POR SESIÓN (FIX DUPLICADOS)
    // =========================================================

    @Override
    @Transactional
    public void registerAttendance(Long sessionId, List<AttendanceMarkDTO> marks) {

        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() == Rol.USER) {
            throw new RuntimeException("No autorizado");
        }

        ClassSession session = classSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));

        Course course = session.getCourse();
        Organization org = session.getOrganization();

        for (AttendanceMarkDTO mark : marks) {

            User student = userRepository.findById(mark.getUserId())
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

            boolean upToDate = paymentService.isStudentUpToDate(
                    student.getId(),
                    course.getId()
            );

            Attendance attendance = attendanceRepository
                    .findByStudentIdAndClassSessionId(student.getId(), sessionId)
                    .orElseGet(Attendance::new);

            attendance.setClassSession(session);
            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setOrganization(org);
            attendance.setAttended(mark.isPresent());
            attendance.setHasDebt(!upToDate);
            attendance.setTakenBy(currentUser);
            attendance.setTakenAt(LocalDateTime.now());

            attendanceRepository.save(attendance);
        }
    }

    // =========================================================
    // 📊 STATS
    // =========================================================

    @Override
    public List<CourseMonthlyAttendanceDTO> getCourseMonthlyStats(
            Long courseId, int month, int year) {
        return attendanceRepository.getMonthlyCourseStats(courseId, month, year);
    }

    // =========================================================
    // 📅 SESIÓN DIARIA
    // =========================================================

    @Override
    public ClassSession getOrCreateTodaySession(Long courseId) {

        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() == Rol.USER) {
            throw new RuntimeException("No autorizado");
        }

        LocalDate today = LocalDate.now();

        return classSessionRepository.findByCourseIdAndDate(courseId, today)
                .orElseGet(() -> {

                    Course course = courseRepository.findById(courseId)
                            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

                    User instructor =
                            course.getInstructor() != null
                                    ? course.getInstructor()
                                    : currentUser;

                    return classSessionRepository.save(
                            ClassSession.builder()
                                    .course(course)
                                    .date(today)
                                    .name(course.getName() + " - " + today)
                                    .instructor(instructor)
                                    .organization(course.getOrganization())
                                    .build()
                    );
                });
    }

    // =========================================================
    // 🔁 LEGACY
    // =========================================================

    @Override
    @Transactional
    public void registerAttendanceByCourse(
            Long courseId,
            Map<Long, Boolean> attendanceMap
    ) {
        registerAttendance(
                getOrCreateTodaySession(courseId).getId(),
                attendanceMap.entrySet()
                        .stream()
                        .map(e -> new AttendanceMarkDTO(e.getKey(), e.getValue()))
                        .toList()
        );
    }
}
