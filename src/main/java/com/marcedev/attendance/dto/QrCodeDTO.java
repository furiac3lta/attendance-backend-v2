package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeDTO {
    private Long classId;
    private String payload;
    private String imageBase64;
    private LocalDateTime expiresAt;
}
