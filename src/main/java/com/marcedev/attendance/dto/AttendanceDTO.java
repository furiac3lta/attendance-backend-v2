package com.marcedev.attendance.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {

    private Long id;

    // Clase
    private Long classSessionId;
    private String className;

    // Alumno
    private Long studentId;
    private String studentName;

    // Curso
    private Long courseId;
    private String courseName;

    // Organización
    private Long organizationId;
    private String organizationName;

    // Estado
    private boolean attended;

    // ✅ Auditoría
    private String takenByName;
    private String takenByRole;
    private LocalDateTime takenAt;

    private boolean hasDebt;

}
