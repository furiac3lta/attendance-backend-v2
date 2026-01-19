// src/main/java/com/marcedev/attendance/controller/UserController.java
package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.InstructorDTO;
import com.marcedev.attendance.dto.*;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.mapper.AttendanceMapper;
import com.marcedev.attendance.repository.AttendanceRepository;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.EnrollmentRepository;
import com.marcedev.attendance.repository.OrganizationRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.PaymentService;
import com.marcedev.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final OrganizationRepository organizationRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;
    private final PaymentService paymentService;
    private final EnrollmentRepository enrollmentRepository;

    // ==========================================================
    // ✅ LISTAR USUARIOS (PAGINADO)
    // ==========================================================
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Long orgId,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Boolean active
    ) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        Page<User> page;
        Boolean activeFilter = active != null ? active : true;

        // 🔥 SUPER ADMIN — puede filtrar todo
        if (currentUser.getRole() == Rol.SUPER_ADMIN) {
            page = userService.filterUsers(search, role, orgId, courseId, activeFilter, pageable);
        }

        // 🔥 ADMIN — solo su organización
        else if (currentUser.getRole() == Rol.ADMIN) {
            Long myOrgId = currentUser.getOrganization() != null
                    ? currentUser.getOrganization().getId()
                    : null;

            page = userService.filterUsers(search, role, myOrgId, courseId, activeFilter, pageable);
        }

        // 🔥 OTROS — no pueden ver usuarios
        else {
            return ResponseEntity.status(403).body(Page.empty());
        }

        Page<UserDTO> dtoPage = page.map(u -> new UserDTO(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getRole().name(),
                u.isActive(),
                (u.getOrganization() != null && !u.getOrganization().isProPlan())
                        ? null
                        : u.getObservations(),
                u.getOrganization() != null ? u.getOrganization().getName() : null,
                u.getOrganization() != null ? u.getOrganization().isProPlan() : false,
                u.getCourses() != null ? u.getCourses().stream().map(c -> c.getName()).toList() : List.of(),
                u.getOrganization() != null ? u.getOrganization().getId() : null
        ));

        return ResponseEntity.ok(dtoPage);
    }

    // ==========================================================
    // ✅ CREAR USUARIO
    // ==========================================================
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserCreateDTO dto) {
        Rol currentRole = getCurrentUserRole();
        if (currentRole == Rol.INSTRUCTOR || currentRole == Rol.USER)
            return ResponseEntity.status(403).body("🚫 Sin permisos.");

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        if (dto.getRole() != null) {
            user.setRole(Rol.valueOf(dto.getRole()));
        }
        user.setPassword(dto.getPassword());
        user.setObservations(dto.getObservations());

        if (currentRole == Rol.SUPER_ADMIN && dto.getOrganizationId() != null) {
            Organization organization = organizationRepository.findByIdAndActiveTrue(dto.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organización no encontrada"));
            user.setOrganization(organization);
        }

        if (currentRole == Rol.ADMIN) {
            var me = userService.findByEmail(getAuthenticatedEmail()).orElseThrow();
            user.setOrganization(me.getOrganization());
            if (user.getRole() == Rol.SUPER_ADMIN)
                return ResponseEntity.status(403).body("🚫 No puede crear SUPER_ADMIN.");
        }
        return ResponseEntity.ok(userService.save(user));
    }

    // ==========================================================
    // 📊 HISTORIAL DEL ALUMNO
    // ==========================================================
    @GetMapping("/{id}/history")
    public ResponseEntity<?> getStudentHistory(@PathVariable Long id) {
        User currentUser = userService.getAuthenticatedUser();

        User target = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (currentUser.getRole() == Rol.USER) {
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body("🚫 No autorizado.");
            }
            target = currentUser;
        } else if (currentUser.getRole() == Rol.ADMIN || currentUser.getRole() == Rol.INSTRUCTOR) {
            if (currentUser.getOrganization() == null
                    || target.getOrganization() == null
                    || !currentUser.getOrganization().getId().equals(target.getOrganization().getId())) {
                return ResponseEntity.status(403).body("🚫 No autorizado.");
            }
        }

        if (target.getRole() != Rol.USER) {
            return ResponseEntity.badRequest().body("⚠️ El historial solo aplica a alumnos.");
        }

        if (target.getOrganization() != null && !target.getOrganization().isProPlan()) {
            return ResponseEntity.status(403).body("🚫 Funcionalidad disponible solo en plan PRO.");
        }

        var courses = enrollmentRepository.findByUserIdAndActiveTrue(id).stream()
                .map(e -> new CourseSummaryDTO(
                        e.getCourse().getId(),
                        e.getCourse().getName(),
                        e.getCourse().getOrganization() != null ? e.getCourse().getOrganization().getName() : null,
                        e.getCourse().isActive()
                ))
                .toList();

        var attendances = attendanceRepository.findActiveByStudentIdOrderByTakenAtDesc(id).stream()
                .map(attendanceMapper::toDTO)
                .toList();

        var payments = paymentService.listByStudent(id);

        StudentHistoryDTO history = new StudentHistoryDTO(
                target.getId(),
                target.getFullName(),
                target.getEmail(),
                target.getObservations(),
                target.getOrganization() != null ? target.getOrganization().getName() : null,
                courses,
                attendances,
                payments
        );

        return ResponseEntity.ok(history);
    }

    // ==========================================================
    // 📥 IMPORTAR USUARIOS DESDE EXCEL/CSV
    // ==========================================================
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importUsers(@RequestParam("file") MultipartFile file) {
        Rol currentRole = getCurrentUserRole();
        if (currentRole != Rol.SUPER_ADMIN && currentRole != Rol.ADMIN) {
            return ResponseEntity.status(403).body("🚫 Sin permisos.");
        }

        UserImportResultDTO result = userService.importUsersFromFile(file);
        return ResponseEntity.ok(result);
    }

    // ==========================================================
    // ✅ EDITAR USUARIO (NUEVO – NECESARIO PARA NETLIFY)
    // ==========================================================
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) {
        UserDTO updated = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updated);
    }

    // ==========================================================
    // 🔒 CAMBIO DE CONTRASEÑA (ADMIN / SUPER_ADMIN)
    // ==========================================================
    @PutMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(
            @PathVariable Long id,
            @RequestBody PasswordUpdateDTO dto
    ) {
        Rol role = getCurrentUserRole();
        if (role != Rol.SUPER_ADMIN && role != Rol.ADMIN) {
            return ResponseEntity.status(403).body("🚫 Sin permisos.");
        }

        userService.changePassword(id, dto.getNewPassword());
        return ResponseEntity.ok("✅ Contraseña actualizada.");
    }

    // ==========================================================
    // ✅ ACTIVAR / DESACTIVAR USUARIO
    // ==========================================================
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        Rol role = getCurrentUserRole();
        if (role != Rol.SUPER_ADMIN && role != Rol.ADMIN) {
            return ResponseEntity.status(403).body("🚫 Sin permisos.");
        }
        userService.deactivateUser(id);
        return ResponseEntity.ok("✅ Usuario desactivado.");
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        Rol role = getCurrentUserRole();
        if (role != Rol.SUPER_ADMIN && role != Rol.ADMIN) {
            return ResponseEntity.status(403).body("🚫 Sin permisos.");
        }
        userService.activateUser(id);
        return ResponseEntity.ok("✅ Usuario activado.");
    }

    // ==========================================================
    // ✅ ASIGNAR CURSOS
    // ==========================================================
    @PostMapping("/{userId}/assign-courses")
    public ResponseEntity<?> assignCourses(@PathVariable Long userId, @RequestBody List<Long> courseIds) {
        Rol role = getCurrentUserRole();
        if (role != Rol.SUPER_ADMIN && role != Rol.ADMIN && role != Rol.INSTRUCTOR)
            return ResponseEntity.status(403).body("🚫 Sin permisos.");

        return ResponseEntity.ok(userService.assignCourses(userId, courseIds));
    }

    // ==========================================================
    // ✅ LISTAR POR ROL
    // ==========================================================
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable Rol role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        List<User> users;
        if (currentUser.getRole() == Rol.SUPER_ADMIN) {
            users = userRepository.findByRoleAndActiveTrue(role);
        } else if (currentUser.getRole() == Rol.ADMIN) {
            if (currentUser.getOrganization() == null) {
                return ResponseEntity.badRequest().body("⚠️ Este admin no tiene organización asignada.");
            }
            users = userRepository.findByRoleAndOrganizationIdAndActiveTrue(
                    role,
                    currentUser.getOrganization().getId()
            );
        } else {
            return ResponseEntity.status(403).body("🚫 No tienes permisos para ver instructores.");
        }

        users.forEach(u -> {
            if (u.getCourses() != null) {
                u.getCourses().forEach(c -> c.setInstructor(null));
            }
        });

        return ResponseEntity.ok(users);
    }

    // ==========================================================
    // 🔧 HELPERS INTERNOS
    // ==========================================================
    private String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private Rol getCurrentUserRole() {
        String email = getAuthenticatedEmail();
        return userService.findByEmail(email).orElseThrow().getRole();
    }

    @GetMapping("/instructors")
    public List<InstructorDTO> getInstructors() {
        return userRepository.findByRoleAndActiveTrue(Rol.INSTRUCTOR)
                .stream()
                .map(u -> new InstructorDTO(
                        u.getId(),
                        u.getFullName(),
                        u.getEmail(),
                        u.getOrganization().getId(),
                        u.getOrganization().getName()
                ))
                .toList();
    }

}
