package com.marcedev.attendance.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"course", "instructor", "organization", "hibernateLazyInitializer", "handler"})
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties({"classes"}) // evita ciclo
    private Course course;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    /** Estado de la clase (soft delete) */
    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    /** Observaciones libres de la clase */
    @Column(columnDefinition = "TEXT")
    private String observations;

    /** QR habilitado para esta clase (solo plan PRO) */
    @Column(name = "qr_enabled", nullable = false)
    @Builder.Default
    private boolean qrEnabled = false;

    /** Token QR de la clase (válido solo el día) */
    @Column(name = "qr_token", length = 128)
    private String qrToken;

    /** Expiración del QR */
    @Column(name = "qr_expires_at")
    private LocalDateTime qrExpiresAt;

}
