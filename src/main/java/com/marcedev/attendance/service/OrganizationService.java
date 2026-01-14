package com.marcedev.attendance.service;

import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.OrganizationRepository;
import com.marcedev.attendance.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    /**
     * 🔹 Desactiva una organización (soft delete).
     */
    @Transactional
    public void deactivateOrganization(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada"));

        org.setActive(false);
        organizationRepository.save(org);
    }

    /**
     * 🔹 Reactiva una organización.
     */
    @Transactional
    public void activateOrganization(Long id) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada"));
        org.setActive(true);
        organizationRepository.save(org);
    }

    /**
     * 🔹 Asigna un administrador (rol ADMIN) a una organización
     */
    @Transactional
    public void assignAdmin(Long organizationId, Long userId) {
        Organization org = organizationRepository.findByIdAndActiveTrue(organizationId)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada."));
        User admin = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (admin.getRole() != Rol.ADMIN) {
            throw new RuntimeException("El usuario seleccionado no tiene rol ADMIN.");
        }

        // ✅ permitir cambio (si querés bloquear, poné una validación acá)
        org.setAdmin(admin);
        admin.setOrganization(org);

        organizationRepository.saveAndFlush(org);   // fuerza UPDATE organizations (admin_id)
        userRepository.saveAndFlush(admin);         // fuerza UPDATE users (organization_id)
    }

}
