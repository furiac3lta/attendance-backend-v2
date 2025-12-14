package com.marcedev.attendance.dto;

import com.marcedev.attendance.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateDTO {
    private Long studentId;
    private Long courseId;
    private int month;
    private int year;
    private BigDecimal amount;
    private PaymentMethod method;
}
