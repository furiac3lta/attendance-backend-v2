package com.marcedev.attendance.repository;

import com.marcedev.attendance.enums.Rol;

public interface DashboardSystemRepository {

    long countOrganizations();
    long countUsers();
    long countByRole(Rol role);
    long countCourses();
    long countActiveEnrollments();
    long countPayments();
}
