package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentHistoryDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String observations;
    private String organizationName;
    private List<CourseSummaryDTO> courses;
    private List<AttendanceDTO> attendances;
    private List<PaymentDTO> payments;
}
