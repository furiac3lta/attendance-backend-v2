package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.EnrollmentDTO;
import com.marcedev.attendance.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentDTO> enroll(
            @RequestParam Long userId,
            @RequestParam Long courseId
    ) {
        return ResponseEntity.ok(
                enrollmentService.enrollUser(userId, courseId)
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> byCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getActiveByCourse(courseId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        enrollmentService.deactivateEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
