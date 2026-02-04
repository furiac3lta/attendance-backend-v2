package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.AttendanceMarkDTO;
import com.marcedev.attendance.dto.ClassCreateDTO;
import com.marcedev.attendance.dto.ClassDetailsDTO;
import com.marcedev.attendance.dto.ClassUpdateDTO;
import com.marcedev.attendance.dto.QrCodeDTO;
import com.marcedev.attendance.entities.ClassSession;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.ClassSessionRepository;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.AttendanceService;
import com.marcedev.attendance.service.ClassService;
import com.marcedev.attendance.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final AttendanceService attendanceService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ClassSessionRepository classSessionRepository;
    private final QrCodeService qrCodeService;

    private static final ZoneId APP_ZONE = ZoneId.of("America/Argentina/Buenos_Aires");

    // ✅ Obtener o crear la clase del día (para tomar asistencia)
    @GetMapping("/today/{courseId}")
    public ResponseEntity<?> getOrCreateTodayClass(@PathVariable Long courseId) {
        return ResponseEntity.ok(classService.getOrCreateTodaySession(courseId));
    }

    // ✅ Obtener clases por curso
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getClassesByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) Boolean active
    ) {
        boolean activeFilter = active != null ? active : true;
        return ResponseEntity.ok(
                activeFilter
                        ? classService.findByCourseId(courseId)
                        : classService.findByCourseIdInactive(courseId)
        );
    }

    @GetMapping("/{id}/details")
    public ClassDetailsDTO getClassDetails(@PathVariable Long id) {
        return classService.getClassDetails(id);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClassCreateDTO dto) {
        try {
            User currentUser = getAuthenticatedUser();

            if (!hasPermission(Rol.INSTRUCTOR, Rol.ADMIN, Rol.SUPER_ADMIN)) {
                return ResponseEntity.status(403).body("🚫 No autorizado para crear clases.");
            }

            var course = courseRepository.findByIdAndActiveTrue(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

            LocalDate date = LocalDate.parse(dto.getDate());

            ClassSession newClass = new ClassSession();
            newClass.setName(dto.getName());
            newClass.setDate(date);
            newClass.setCourse(course);
            newClass.setInstructor(currentUser); // ✅ NECESARIO
            newClass.setOrganization(course.getOrganization()); // ✅ NECESARIO
            newClass.setObservations(dto.getObservations());

            ClassSession saved = classService.create(newClass);

            return ResponseEntity.ok(Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "date", saved.getDate(),
                    "courseName", course.getName()
            ));

        } catch (Exception e) {
            e.printStackTrace(); // ✅ MOSTRAR ERROR EN CONSOLA
            return ResponseEntity.internalServerError()
                    .body("❌ Error inesperado: " + e.getMessage());
        }
    }

    // ✅ Actualizar observaciones de clase
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ClassUpdateDTO dto) {
        User currentUser = getAuthenticatedUser();

        if (!hasPermission(Rol.INSTRUCTOR, Rol.ADMIN, Rol.SUPER_ADMIN)) {
            return ResponseEntity.status(403).body("🚫 No autorizado para editar clases.");
        }

        ClassSession session = classSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (currentUser.getRole() == Rol.INSTRUCTOR) {
            if (session.getInstructor() == null
                    || !session.getInstructor().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).body("🚫 Solo el instructor de la clase puede editarla.");
            }
        }

        session.setObservations(dto.getObservations());
        classSessionRepository.save(session);

        return ResponseEntity.ok(Map.of(
                "id", session.getId(),
                "observations", session.getObservations()
        ));
    }

    // ✅ Obtener una clase por ID (para tomar asistencia)
    @GetMapping("/{id}")
    public ResponseEntity<?> getClassById(@PathVariable Long id) {
        ClassSession session = classService.findById(id);
        if (session == null) {
            return ResponseEntity.status(404).body("❌ Clase no encontrada");
        }

        // Evitar serialización recursiva
        if (session.getInstructor() != null) session.getInstructor().setPassword(null);
        if (session.getCourse() != null && session.getCourse().getInstructor() != null) {
            session.getCourse().getInstructor().setPassword(null);
        }

        return ResponseEntity.ok(session);
    }


    // ✅ Registrar asistencia (CORREGIDO)
    @PostMapping("/{classId}/attendance")
    public ResponseEntity<?> registerAttendance(
            @PathVariable Long classId,
            @RequestBody List<com.marcedev.attendance.dto.AttendanceMarkDTO> attendances
    ) {
        attendanceService.registerAttendance(classId, attendances);
        return ResponseEntity.ok().build(); // ✅ Respuesta simple, sin devolver nada
    }


    // ✅ Obtener asistencias registradas
    @GetMapping("/{classId}/attendance")
    public ResponseEntity<?> getAttendance(@PathVariable Long classId) {
        return ResponseEntity.ok(attendanceService.findByClassId(classId));
    }

    // ✅ Obtener alumnos para tomar asistencia
    @GetMapping("/{classId}/students")
    public ResponseEntity<?> getStudentsForClass(@PathVariable Long classId) {

        ClassSession classSession = classService.findById(classId);
        if (classSession == null) {
            return ResponseEntity.status(404).body("❌ Clase no encontrada");
        }

        Course course = classSession.getCourse();
        if (course == null || course.getStudents() == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(
                course.getStudents().stream()
                        .filter(User::isActive)
                        .map(this::mapStudent)
                        .collect(Collectors.toList())
        );
    }

    // ✅ Mapear alumno → JSON simple
    private Map<String, Object> mapStudent(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("fullName", user.getFullName());
        map.put("email", user.getEmail());
        map.put("role", user.getRole().name());
        return map;
    }

    // ✅ Permisos
    private boolean hasPermission(Rol... allowed) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        User user = userRepository.findByEmailAndActiveTrue(auth.getName()).orElse(null);
        if (user == null) return false;
        if (user.getRole() == Rol.SUPER_ADMIN) return true;

        return Arrays.asList(allowed).contains(user.getRole());
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() != Rol.SUPER_ADMIN) {
            return ResponseEntity.status(403).body("🚫 Solo SUPER_ADMIN puede desactivar clases.");
        }

        classService.deactivateClass(id);
        return ResponseEntity.ok("✅ Clase desactivada.");
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() != Rol.SUPER_ADMIN) {
            return ResponseEntity.status(403).body("🚫 Solo SUPER_ADMIN puede activar clases.");
        }

        classService.activateClass(id);
        return ResponseEntity.ok("✅ Clase activada.");
    }

    @PostMapping("/create-or-get")
    public ResponseEntity<?> createOrGetSession(@RequestBody Map<String, Long> body) {
        Long courseId = body.get("courseId");
        ClassSession session = classService.getOrCreateTodaySession(courseId);
        return ResponseEntity.ok(session);
    }

    // ✅ Generar QR para una clase (solo plan PRO)
    @PostMapping("/{classId}/qr")
    public ResponseEntity<?> generateQr(@PathVariable Long classId) {
        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() == Rol.USER) {
            return ResponseEntity.status(403).body("🚫 Sin permisos.");
        }

        ClassSession session = classSessionRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (!session.isActive()) {
            return ResponseEntity.status(400).body("⚠️ La clase está desactivada.");
        }

        if (session.getCourse() == null || session.getCourse().getOrganization() == null
                || !session.getCourse().getOrganization().isProPlan()) {
            return ResponseEntity.status(403).body("🚫 Funcionalidad disponible solo en plan PRO.");
        }

        if (currentUser.getRole() == Rol.INSTRUCTOR) {
            if (session.getInstructor() == null
                    || !session.getInstructor().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).body("🚫 Solo el instructor de la clase puede generar el QR.");
            }
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = LocalDate.now(APP_ZONE).atTime(LocalTime.MAX);
        String payload = "ATTENDANCE:CLASS:" + session.getId() + ":TOKEN:" + token;

        session.setQrEnabled(true);
        session.setQrToken(token);
        session.setQrExpiresAt(expiresAt);
        classSessionRepository.save(session);

        String base64 = qrCodeService.generateBase64Png(payload, 320);

        return ResponseEntity.ok(new QrCodeDTO(
                session.getId(),
                payload,
                "data:image/png;base64," + base64,
                expiresAt
        ));
    }
    // ✅ Obtener usuario autenticado
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("⚠️ Usuario no autenticado");
        }

        String email = auth.getName();
        return userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("❌ Usuario no encontrado en BD"));
    }

}
