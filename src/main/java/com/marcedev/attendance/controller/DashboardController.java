package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.service.DashboardService;
import com.marcedev.attendance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

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
}
