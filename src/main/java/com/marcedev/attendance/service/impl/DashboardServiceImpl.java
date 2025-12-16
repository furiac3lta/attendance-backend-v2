package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.OrganizationDashboardDTO;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.PaymentMethod;
import com.marcedev.attendance.repository.DashboardRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    @Override
    public OrganizationDashboardDTO getOrganizationDashboard(YearMonth month) {

        // ================= SEGURIDAD =================
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Usuario autenticado no encontrado")
                );

        Long organizationId = admin.getOrganization().getId();

        // ================= FECHA =================
        int year = month.getYear();
        int monthValue = month.getMonthValue();

        // ================= ALUMNOS =================
        long totalStudents =
                dashboardRepository.countActiveStudents(organizationId);

        Map<String, Long> studentsByCourse = new HashMap<>();
        dashboardRepository.countStudentsByCourse(organizationId)
                .forEach(row ->
                        studentsByCourse.put(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        )
                );

        // ================= PAGOS =================
        long paidStudents =
                dashboardRepository.countPaidStudents(
                        organizationId, year, monthValue
                );

        long debtStudents =
                dashboardRepository.countDebtStudents(
                        organizationId, year, monthValue
                );

        // ================= RECAUDACIÓN =================
        BigDecimal totalRevenue =
                dashboardRepository.totalRevenue(
                        organizationId, year, monthValue
                );

        if (totalRevenue == null) {
            totalRevenue = BigDecimal.ZERO;
        }

        Map<String, BigDecimal> revenueByCourse = new HashMap<>();
        dashboardRepository.revenueByCourse(
                        organizationId, year, monthValue
                )
                .forEach(row ->
                        revenueByCourse.put(
                                (String) row[0],
                                (BigDecimal) row[1]
                        )
                );

        // ================= MÉTODOS DE PAGO =================
        BigDecimal cash =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId,
                        year,
                        monthValue,
                        PaymentMethod.CASH
                );

        BigDecimal transfer =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId,
                        year,
                        monthValue,
                        PaymentMethod.TRANSFER
                );

        BigDecimal mercadoPago =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId,
                        year,
                        monthValue,
                        PaymentMethod.MERCADOPAGO
                );

        // null safety
        cash = cash != null ? cash : BigDecimal.ZERO;
        transfer = transfer != null ? transfer : BigDecimal.ZERO;
        mercadoPago = mercadoPago != null ? mercadoPago : BigDecimal.ZERO;

        return new OrganizationDashboardDTO(
                totalStudents,
                studentsByCourse,
                paidStudents,
                debtStudents,
                totalRevenue,
                revenueByCourse,
                cash,
                transfer,
                mercadoPago
        );
    }

}
