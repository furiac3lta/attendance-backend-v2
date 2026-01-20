package com.marcedev.attendance.dto;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String fullName;
    private String email;
    private String password;
    private String role;
    private Long organizationId;
    private String observations;
    private String dni;
    private String phone;
    private String address;
}
