package com.marcedev.attendance.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private String universityProgram;

    private Long instructorId;
    private String instructorName;

    // 🔹 Nuevos campos de organización
    private Long organizationId;
    private String organizationName;

    private boolean active;
}
