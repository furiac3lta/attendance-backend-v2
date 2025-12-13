package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.AttendanceDTO;
import com.marcedev.attendance.dto.AttendanceMarkDTO;
import com.marcedev.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // =====================================================
    // CRUD BÁSICO
    // =====================================================

    @PostMapping
    public ResponseEntity<AttendanceDTO> save(@RequestBody AttendanceDTO dto) {
        return ResponseEntity.ok(attendanceService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceDTO>> getAll() {
        return ResponseEntity.ok(attendanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // CONSULTAS
    // =====================================================

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<AttendanceDTO>> getByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(attendanceService.findByClassId(classId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(attendanceService.findByCourseId(courseId));
    }

    // =====================================================
    // SESIONES
    // =====================================================

    @PostMapping("/course/{courseId}/session/today")
    public ResponseEntity<?> getOrCreateTodaySession(@PathVariable Long courseId) {
        var session = attendanceService.getOrCreateTodaySession(courseId);
        return ResponseEntity.ok(Map.of(
                "id", session.getId(),
                "date", session.getDate(),
                "name", session.getName()
        ));
    }

    // =====================================================
    // REGISTRAR ASISTENCIA (API NUEVA — CORRECTA)
    // =====================================================

    @PostMapping("/session/{sessionId}")
    public ResponseEntity<?> registerAttendance(
            @PathVariable Long sessionId,
            @RequestBody List<AttendanceMarkDTO> attendances
    ) {
        attendanceService.registerAttendance(sessionId, attendances);
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // 🔴 COMPATIBILIDAD CON API VIEJA (FIX CLAVE)
    // =====================================================
    // Esto evita 401/403 si el front todavía llama:
    // POST /api/attendance/{id}/attendance
    // =====================================================

    @PostMapping("/{sessionId}/attendance")
    public ResponseEntity<?> registerAttendanceLegacy(
            @PathVariable Long sessionId,
            @RequestBody List<AttendanceMarkDTO> attendances
    ) {
        attendanceService.registerAttendance(sessionId, attendances);
        return ResponseEntity.ok().build();
    }

    // =====================================================
    // REPORTES
    // =====================================================

    @GetMapping("/course/{courseId}/monthly")
    public ResponseEntity<?> getMonthlyStats(
            @PathVariable Long courseId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                attendanceService.getCourseMonthlyStats(courseId, month, year)
        );
    }
}
