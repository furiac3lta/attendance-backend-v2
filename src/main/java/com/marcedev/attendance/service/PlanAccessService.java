package com.marcedev.attendance.service;

import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PlanAccessService {

    private static final int FREE_MAX_ACTIVE_STUDENTS = 20;
    private static final int FREE_MAX_ACTIVE_INSTRUCTORS = 1;
    private static final int FREE_HISTORY_DAYS = 30;

    private final UserRepository userRepository;

    public boolean isProPlan(Organization organization) {
        return organization != null && organization.isProPlan();
    }

    public void requirePro(Organization organization, String featureLabel) {
        if (organization != null && !organization.isProPlan()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "🚫 " + featureLabel + " disponible solo en plan PRO."
            );
        }
    }

    public void validateActiveStudentLimit(Organization organization, boolean addingStudent) {
        if (!addingStudent || organization == null || organization.isProPlan()) {
            return;
        }

        long activeStudents =
                userRepository.countActiveStudentsByOrganization(organization.getId());

        if (activeStudents >= FREE_MAX_ACTIVE_STUDENTS) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "🚫 Límite de " + FREE_MAX_ACTIVE_STUDENTS + " alumnos activos para plan FREE."
            );
        }
    }

    public void validateInstructorLimit(Organization organization, boolean addingInstructor) {
        if (!addingInstructor || organization == null || organization.isProPlan()) {
            return;
        }

        long activeInstructors =
                userRepository.countActiveInstructorsByOrganization(organization.getId());

        if (activeInstructors >= FREE_MAX_ACTIVE_INSTRUCTORS) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "🚫 Solo se permite " + FREE_MAX_ACTIVE_INSTRUCTORS + " instructor en plan FREE."
            );
        }
    }

    public LocalDate freeHistoryStartDate() {
        return LocalDate.now().minusDays(FREE_HISTORY_DAYS);
    }
}
