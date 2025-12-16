package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.EnrollmentDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Enrollment;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.mapper.EnrollmentMapper;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.EnrollmentRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentDTO enrollUser(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .active(true)
                .build();

        return enrollmentMapper.toDTO(
                enrollmentRepository.save(enrollment)
        );
    }

    @Override
    @Transactional
    public void deactivateEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        enrollment.setActive(false);
    }

    @Override
    public List<EnrollmentDTO> getActiveByCourse(Long courseId) {
        return enrollmentRepository.findByCourseIdAndActiveTrue(courseId)
                .stream()
                .map(enrollmentMapper::toDTO)
                .toList();
    }

    @Override
    public long countActiveByOrganization(Long organizationId) {
        return enrollmentRepository.countActiveByOrganization(organizationId);
    }
}
