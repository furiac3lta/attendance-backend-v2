package com.marcedev.attendance.dto;

import java.math.BigDecimal;
import java.util.Map;

public record OrganizationDashboardDTO(

        // Alumnos
        long totalActiveStudents,
        Map<String, Long> studentsByCourse,

        // Pagos
        long paidStudents,
        long debtStudents,

        // Recaudación
        BigDecimal totalRevenue,
        Map<String, BigDecimal> revenueByCourse,

        // Métodos de pago
        BigDecimal cashAmount,
        BigDecimal transferAmount,
        BigDecimal mercadoPagoAmount
) {}
