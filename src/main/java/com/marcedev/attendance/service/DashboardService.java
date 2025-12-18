package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.AdminDashboardDTO;
import com.marcedev.attendance.dto.DebtorDTO;
import com.marcedev.attendance.dto.OrganizationDashboardDTO;

import java.time.YearMonth;
import java.util.List;

public interface DashboardService {

    // Dashboard por organización (ADMIN / SUPER)
    OrganizationDashboardDTO getOrganizationDashboard(YearMonth month);

    // 🔥 Dashboard ADMIN global (pagaron vs no pagaron)
    AdminDashboardDTO getAdminDashboard();

    List<DebtorDTO> getDebtors();
}
