package com.marcedev.attendance.dto;


import java.math.BigDecimal;

public record AdminDashboardDTO(
        String organizationName,
        long activeStudents,
        long paidStudents,
        long unpaidStudents,
        BigDecimal totalIncome
) {}
