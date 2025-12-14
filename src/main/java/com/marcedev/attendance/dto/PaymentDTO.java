package com.marcedev.attendance.dto;

import com.marcedev.attendance.enums.PaymentMethod;
import com.marcedev.attendance.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private int month;
    private int year;

    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;

    private LocalDateTime paidAt;
}
