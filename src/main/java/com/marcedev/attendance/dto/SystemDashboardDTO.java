package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemDashboardDTO {

    private long totalOrganizations;
    private long totalUsers;
    private long totalStudents;
    private long totalInstructors;
    private long totalCourses;
    private long totalActiveEnrollments;
    private long totalPayments;
}
