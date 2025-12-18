package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;
import com.marcedev.attendance.entities.Enrollment;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.Payment;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.PaymentMethod;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.*;
import com.marcedev.attendance.service.DashboardService;
import com.marcedev.attendance.service.PaymentService;
import com.marcedev.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;
    private final AttendanceRepository attendanceRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PaymentService paymentService; // ✅ USAR ESTE
private final CourseRepository courseRepository;
private final EnrollmentRepository enrollmentRepository;
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

        long paidStudents =
                paymentRepository.countPaidStudentsByOrganization(
                        organizationId, year, monthValue
                );

        long debtStudents = totalStudents - paidStudents;

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
    // 🧭 DASHBOARD ADMIN (ADMIN de la ORGANIZACIÓN)
    // =====================================================
    @Override
    public AdminDashboardDTO getAdminDashboard() {

        User currentUser = userService.getAuthenticatedUser();

        if (currentUser.getRole() != Rol.ADMIN) {
            throw new RuntimeException("Solo ADMIN puede acceder a este dashboard");
        }

        Organization org = currentUser.getOrganization();
        if (org == null) {
            throw new RuntimeException("El admin no tiene organización asignada");
        }

        String organizationName = org.getName();
        Long orgId = org.getId();

        YearMonth now = YearMonth.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        long activeStudents =
                attendanceRepository.countDistinctStudentsByOrganization(orgId);

        long paidStudents =
                paymentRepository.countPaidStudentsByOrganization(
                        orgId, year, month
                );

        long unpaidStudents = activeStudents - paidStudents;

        BigDecimal totalIncome =
                paymentRepository.sumPaidAmountByOrganization(orgId);

        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;

        return new AdminDashboardDTO(
                organizationName,
                activeStudents,
                paidStudents,
                unpaidStudents,
                totalIncome
        );
    }

    // =====================================================
    // 🚨 MOROSOS (ADMIN)
    // =====================================================
    @Override
    public List<DebtorDTO> getDebtors() {

        User admin = userService.getAuthenticatedUser();

        YearMonth now = YearMonth.now();

        return paymentRepository.findDebtorsByOrganization(
                admin.getOrganization().getId(),
                now.getMonthValue(),
                now.getYear()
        );
    }

}
