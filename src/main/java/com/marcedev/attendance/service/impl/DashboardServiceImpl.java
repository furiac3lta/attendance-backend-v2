package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.PaymentMethod;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.AttendanceRepository;
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
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    // =====================================================
    // 🔐 AUTH
    // =====================================================
    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No hay usuario autenticado");
        }

        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // =====================================================
    // 📊 DASHBOARD ORGANIZACIÓN (ADMIN)
    // =====================================================
    @Override
    public OrganizationDashboardDTO getOrganizationDashboard(YearMonth month) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        if (admin.getOrganization() == null) {
            throw new RuntimeException("El usuario no tiene organización asignada");
        }

        Long organizationId = admin.getOrganization().getId();

        int year = month.getYear();
        int monthValue = month.getMonthValue();

        // ================= ALUMNOS =================
        long totalStudents =
                attendanceRepository.countDistinctStudentsByOrganization(organizationId);

        long debtStudents =
                attendanceRepository.countStudentsWithDebt(organizationId);

        long paidStudents = totalStudents - debtStudents;

        // ================= ALUMNOS POR CURSO =================
        Map<String, Long> studentsByCourse = new HashMap<>();
        dashboardRepository.countStudentsByCourse(organizationId)
                .forEach(row ->
                        studentsByCourse.put(
                                (String) row[0],
                                ((Number) row[1]).longValue()
                        )
                );

        // ================= RECAUDACIÓN =================
        BigDecimal totalRevenue =
                dashboardRepository.totalRevenue(organizationId, year, monthValue);

        totalRevenue = totalRevenue != null ? totalRevenue : BigDecimal.ZERO;

        Map<String, BigDecimal> revenueByCourse = new HashMap<>();
        dashboardRepository.revenueByCourse(organizationId, year, monthValue)
                .forEach(row ->
                        revenueByCourse.put(
                                (String) row[0],
                                (BigDecimal) row[1]
                        )
                );

        // ================= MÉTODOS DE PAGO =================
        BigDecimal cash =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId, year, monthValue, PaymentMethod.CASH);

        BigDecimal transfer =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId, year, monthValue, PaymentMethod.TRANSFER);

        BigDecimal mercadoPago =
                dashboardRepository.revenueByPaymentMethod(
                        organizationId, year, monthValue, PaymentMethod.MERCADOPAGO);

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

    // =====================================================
    // 🧭 DASHBOARD ADMIN GLOBAL (ADMIN / SUPER_ADMIN)
    // =====================================================
    @Override
    public AdminDashboardDTO getAdminDashboard() {

        User currentUser = getAuthenticatedUser();

        if (currentUser.getRole() != Rol.ADMIN
                && currentUser.getRole() != Rol.SUPER_ADMIN) {
            throw new RuntimeException("No autorizado");
        }

        Long orgId = currentUser.getOrganization().getId();

        long totalStudents =
                attendanceRepository.countDistinctStudentsByOrganization(orgId);

        long studentsWithDebt =
                attendanceRepository.countStudentsWithDebt(orgId);

        long studentsUpToDate = totalStudents - studentsWithDebt;

        return new AdminDashboardDTO(
                totalStudents,
                studentsUpToDate,
                studentsWithDebt
        );
    }
}
