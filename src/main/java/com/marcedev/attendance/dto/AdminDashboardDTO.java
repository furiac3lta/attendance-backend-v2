package com.marcedev.attendance.dto;

public record AdminDashboardDTO(
        long totalStudents,
        long studentsUpToDate,
        long studentsWithDebt
) {}
