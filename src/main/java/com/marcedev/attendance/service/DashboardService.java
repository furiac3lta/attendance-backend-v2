package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;

import java.time.YearMonth;

public interface DashboardService {

    OrganizationDashboardDTO getOrganizationDashboard(YearMonth month);
    // Dashboard por organización (ADMIN / SUPER)

    // 🔥 NUEVO — Dashboard ADMIN limpio (al día vs deuda)
    AdminDashboardDTO getAdminDashboard();
}
