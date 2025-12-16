package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.PaymentCreateDTO;
import com.marcedev.attendance.dto.PaymentCreateRequest;
import com.marcedev.attendance.dto.PaymentDTO;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    PaymentDTO createPayment(PaymentCreateDTO dto);
    List<PaymentDTO> listByCourse(Long courseId, int month, int year);
    List<PaymentDTO> listByStudent(Long studentId);
    void registerPayment(PaymentCreateRequest request);
    boolean isStudentUpToDate(Long studentId, Long courseId);

    Map<Long, Boolean> getPaymentStatusByCourse(Long courseId, int month, int year);
    boolean isStudentPaid(Long studentId, Long courseId, int month, int year);
}
