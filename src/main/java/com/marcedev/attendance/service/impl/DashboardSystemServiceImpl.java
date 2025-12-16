package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.SystemDashboardDTO;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.DashboardSystemRepository;
import com.marcedev.attendance.service.DashboardSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardSystemServiceImpl implements DashboardSystemService {

    private final DashboardSystemRepository repository;

    @Override
    public SystemDashboardDTO getSystemDashboard() {

        return new SystemDashboardDTO(
                repository.countOrganizations(),
                repository.countUsers(),
                repository.countByRole(Rol.USER),
                repository.countByRole(Rol.INSTRUCTOR),
                repository.countCourses(),
                repository.countActiveEnrollments(),
                repository.countPayments()
        );
    }
}
