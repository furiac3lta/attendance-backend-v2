package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {

    EnrollmentDTO enrollUser(Long userId, Long courseId);

    void deactivateEnrollment(Long enrollmentId);

    List<EnrollmentDTO> getActiveByCourse(Long courseId);

    long countActiveByOrganization(Long organizationId);
}
