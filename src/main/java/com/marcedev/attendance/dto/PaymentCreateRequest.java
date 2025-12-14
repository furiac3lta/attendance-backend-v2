package com.marcedev.attendance.dto;

import com.marcedev.attendance.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentCreateRequest {

    private Long studentId;
    private Long courseId;
    private int month;
    private int year;
    private BigDecimal amount;
    private PaymentMethod method;
}
