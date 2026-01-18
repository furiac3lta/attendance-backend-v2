package com.marcedev.attendance.service;

import com.marcedev.attendance.dto.UserDTO;
import com.marcedev.attendance.dto.UserImportResultDTO;
import com.marcedev.attendance.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * 🔹 Interfaz del servicio de usuarios
 * Maneja la lógica de negocio relacionada con la gestión de usuarios.
 */
public interface UserService {

    List<User> findAll();                            // Obtener todos los usuarios
    Optional<User> findById(Long id);                // Buscar usuario por ID
    Optional<User> findByEmail(String email);        // Buscar usuario por email
    User save(User user);                            // Crear nuevo usuario (con asignación automática de organización)
    User assignCourses(Long userId, List<Long> courseIds); // Asignar cursos a un usuario (devuelve el usuario actualizado)
    void updateUser(Long id, User updatedUser);      // Actualizar datos del usuario
    User createAdminForOrganization(Long organizationId, User newAdminData);
    UserDTO updateUser(Long id, UserDTO dto);
    Page<User> filterUsers(
            String search,
            String role,
            Long orgId,
            Long courseId,
            Boolean active,
            Pageable pageable
    ) ;

    User getAuthenticatedUser();

    UserImportResultDTO importUsersFromFile(MultipartFile file);

    void deactivateUser(Long id);

    void activateUser(Long id);

    void changePassword(Long id, String newPassword);
}
