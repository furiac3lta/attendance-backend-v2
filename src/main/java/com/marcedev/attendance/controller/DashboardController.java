package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.repository.PaymentRepository;
import com.marcedev.attendance.service.DashboardService;
import com.marcedev.attendance.service.PaymentService;
import com.marcedev.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final PaymentService paymentService;

    // 🔹 DASHBOARD ORGANIZACIÓN
    @GetMapping("/organization")
    public ResponseEntity<OrganizationDashboardDTO> getDashboard(
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth month
    ) {
        return ResponseEntity.ok(
                dashboardService.getOrganizationDashboard(month)
        );
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminDashboardDTO adminDashboard() {
        return dashboardService.getAdminDashboard();
    }

    @GetMapping("/admin/debtors")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DebtorDTO> getDebtors(Authentication authentication) {

        // 🔐 viene como UserDetails
        String email = authentication.getName();

        User admin = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin no encontrado"));

        return paymentService.getDebtorsByOrganization(
                admin.getOrganization().getId()
        );
    }



}
