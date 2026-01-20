package com.marcedev.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private boolean active;
    private String observations;
    private String dni;
    private String phone;
    private String address;

    private String organizationName; // Ej: "Irmãos Club Puerto Rico"
    private Boolean organizationProPlan;
    private List<String> courses; // Ej: ["BJJ Kids", "BJJ Adultos"]

    private Long organizationId; // Para formularios (opcional)
}
