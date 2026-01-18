package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSummaryDTO {
    private Long id;
    private String name;
    private String organizationName;
    private boolean active;
}
