package com.marcedev.attendance.entities;

import com.marcedev.attendance.enums.PaymentMethod;
import com.marcedev.attendance.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"student_id", "month", "year"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private int month;   // 1-12
    private int year;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PAID / UNPAID

    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // CASH / TRANSFER / MP / OTHER

    private BigDecimal amount;

    private LocalDateTime paidAt;
}
