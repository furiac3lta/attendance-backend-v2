package com.marcedev.attendance.dto;

public record InstructorDTO(
        Long id,
        String fullName,
        String email,
        Long organizationId,
        String organizationName
) {}
