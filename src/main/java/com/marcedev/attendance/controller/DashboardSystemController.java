package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.SystemDashboardDTO;
import com.marcedev.attendance.service.DashboardSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/system")
@RequiredArgsConstructor
public class DashboardSystemController {

    private final DashboardSystemService dashboardSystemService;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public SystemDashboardDTO getSystemDashboard() {
        return dashboardSystemService.getSystemDashboard();
    }
}
