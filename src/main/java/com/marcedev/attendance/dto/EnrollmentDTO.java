package com.marcedev.attendance.dto;

import java.time.LocalDate;

public record EnrollmentDTO(
        Long id,
        Long userId,
        String userName,
        Long courseId,
        String courseName,
        boolean active,
        LocalDate startDate
) {}
