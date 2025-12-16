package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.PaymentCreateDTO;
import com.marcedev.attendance.dto.PaymentCreateRequest;
import com.marcedev.attendance.dto.PaymentDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Enrollment;
import com.marcedev.attendance.entities.Payment;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.PaymentStatus;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.EnrollmentRepository;
import com.marcedev.attendance.repository.PaymentRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    // ========================= CREATE PAYMENT =========================
    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentCreateDTO dto) {

        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        boolean alreadyPaid = paymentRepository
                .existsByStudentIdAndCourseIdAndMonthAndYearAndStatus(
                        student.getId(),
                        course.getId(),
                        dto.getMonth(),
                        dto.getYear(),
                        PaymentStatus.PAID
                );

        if (alreadyPaid) {
            throw new RuntimeException("Ese alumno ya tiene el mes pago");
        }

        Payment payment = Payment.builder()
                .student(student)
                .course(course)
                .month(dto.getMonth())
                .year(dto.getYear())
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .status(PaymentStatus.PAID)
                .paidAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);

        // 🔥 ENROLLMENT (CLAVE)
        createOrActivateEnrollment(student, course);

        return toDTO(saved);
    }

    // ========================= LISTADOS =========================
    @Override
    public List<PaymentDTO> listByCourse(Long courseId, int month, int year) {
        return paymentRepository.findByCourseMonthYear(courseId, month, year)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<PaymentDTO> listByStudent(Long studentId) {
        return paymentRepository.findByStudent(studentId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // ========================= STATUS =========================
    @Override
    public Map<Long, Boolean> getPaymentStatusByCourse(Long courseId, int month, int year) {

        List<User> students = userRepository.findStudentsByCourseId(courseId);
        Map<Long, Boolean> result = new HashMap<>();

        for (User student : students) {
            boolean paid = isStudentPaid(student.getId(), courseId, month, year);
            result.put(student.getId(), paid);
        }

        return result;
    }

    @Override
    public boolean isStudentPaid(Long studentId, Long courseId, int month, int year) {
        return paymentRepository
                .existsByStudentIdAndCourseIdAndMonthAndYearAndStatus(
                        studentId,
                        courseId,
                        month,
                        year,
                        PaymentStatus.PAID
                );
    }

    // ========================= REGISTER PAYMENT =========================
    @Override
    @Transactional
    public void registerPayment(PaymentCreateRequest request) {

        if (paymentRepository.existsByStudentIdAndMonthAndYear(
                request.getStudentId(),
                request.getMonth(),
                request.getYear()
        )) {
            throw new RuntimeException("El alumno ya tiene pago registrado este mes");
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Payment payment = new Payment();
        payment.setStudent(student);
        payment.setCourse(course);
        payment.setMonth(request.getMonth());
        payment.setYear(request.getYear());
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // 🔥 ENROLLMENT (CLAVE)
        createOrActivateEnrollment(student, course);
    }

    // ========================= ENROLLMENT LOGIC =========================
    private void createOrActivateEnrollment(User student, Course course) {

        Enrollment enrollment = enrollmentRepository
                .findByUserIdAndCourseId(student.getId(), course.getId())
                .orElse(null);

        if (enrollment == null) {
            enrollment = new Enrollment();
            enrollment.setUser(student);
            enrollment.setCourse(course);
            enrollment.setActive(true);
            enrollment.setStartDate(LocalDate.now());
        } else {
            enrollment.setActive(true);
        }

        enrollmentRepository.save(enrollment);
    }

    // ========================= DTO =========================
    private PaymentDTO toDTO(Payment p) {
        return PaymentDTO.builder()
                .id(p.getId())
                .studentId(p.getStudent().getId())
                .studentName(p.getStudent().getFullName())
                .courseId(p.getCourse().getId())
                .courseName(p.getCourse().getName())
                .month(p.getMonth())
                .year(p.getYear())
                .amount(p.getAmount())
                .method(p.getMethod())
                .status(p.getStatus())
                .paidAt(p.getPaidAt())
                .build();
    }
}
