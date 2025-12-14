package com.marcedev.attendance.controller;

import com.marcedev.attendance.dto.PaymentCreateDTO;
import com.marcedev.attendance.dto.PaymentDTO;
import com.marcedev.attendance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ===============================
    // 1️⃣ ALTA DE PAGO
    // ===============================
    @PostMapping
    public ResponseEntity<PaymentDTO> create(
            @RequestBody PaymentCreateDTO dto
    ) {
        PaymentDTO payment = paymentService.createPayment(dto);
        return ResponseEntity.ok(payment);
    }

    // ===============================
    // 2️⃣ LISTAR PAGOS DE UN CURSO
    // ===============================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<PaymentDTO>> listByCourse(
            @PathVariable Long courseId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                paymentService.listByCourse(courseId, month, year)
        );
    }

    // ===============================
    // 3️⃣ LISTAR PAGOS DE UN ALUMNO
    // ===============================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PaymentDTO>> listByStudent(
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                paymentService.listByStudent(studentId)
        );
    }

    // ===============================
    // 4️⃣ STATUS DE PAGO POR CURSO
    // (USADO EN ASISTENCIA)
    // ===============================
    @GetMapping("/status/course/{courseId}")
    public ResponseEntity<Map<Long, Boolean>> statusByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        int m = (month != null)
                ? month
                : LocalDate.now().getMonthValue();

        int y = (year != null)
                ? year
                : LocalDate.now().getYear();

        return ResponseEntity.ok(
                paymentService.getPaymentStatusByCourse(courseId, m, y)
        );
    }
}
