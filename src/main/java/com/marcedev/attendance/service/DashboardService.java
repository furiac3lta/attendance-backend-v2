package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.OrganizationDashboardDTO;

import java.time.YearMonth;

public interface DashboardService {

    OrganizationDashboardDTO getOrganizationDashboard(YearMonth month);
}
