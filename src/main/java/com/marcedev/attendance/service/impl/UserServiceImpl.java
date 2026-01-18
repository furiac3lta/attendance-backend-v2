package com.marcedev.attendance.service.impl;

import com.marcedev.attendance.dto.GeneratedPasswordDTO;
import com.marcedev.attendance.dto.UserDTO;
import com.marcedev.attendance.dto.UserImportResultDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import com.marcedev.attendance.repository.CourseRepository;
import com.marcedev.attendance.repository.OrganizationRepository;
import com.marcedev.attendance.repository.UserRepository;
import com.marcedev.attendance.service.PlanAccessService;
import com.marcedev.attendance.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 🧩 Implementación del servicio de usuarios.
 * Incluye lógica de creación, actualización, asignación de cursos
 * y creación de administradores de organización.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanAccessService planAccessService;

    private static final int GENERATED_PASSWORD_LENGTH = 8;
    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}");
    private static final DataFormatter DATA_FORMATTER = new DataFormatter();
    private static final SecureRandom RANDOM = new SecureRandom();

    // 🔹 Obtener todos los usuarios
    @Override
    public List<User> findAll() {
        return userRepository.findByActiveTrue();
    }

    // 🔹 Buscar usuario por ID
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findByIdAndActiveTrue(id);
    }

    // 🔹 Buscar usuario por email
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailAndActiveTrue(email);
    }

    /**
     * 🔹 Crear nuevo usuario
     * Si el usuario autenticado pertenece a una organización, el nuevo usuario
     * se crea automáticamente dentro de esa organización.
     */
    @Override
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String email = auth.getName();

            userRepository.findByEmailAndActiveTrue(email).ifPresent(currentUser -> {
                if (currentUser.getRole() == Rol.SUPER_ADMIN) {
                    return;
                }

                Organization org = currentUser.getOrganization();

                if (org != null && user.getOrganization() == null) {
                    user.setOrganization(org);
                }

                if (org != null && user.getOrganization() != null
                        && !org.getId().equals(user.getOrganization().getId())) {
                    throw new RuntimeException("No puedes asignar usuarios a otra organización");
                }
            });
        }

        Organization org = user.getOrganization();
        if (org != null && user.getId() == null) {
            if (user.getRole() == Rol.USER && user.isActive()) {
                planAccessService.validateActiveStudentLimit(org, true);
            }
            if (user.getRole() == Rol.INSTRUCTOR && user.isActive()) {
                planAccessService.validateInstructorLimit(org, true);
            }
            if (!org.isProPlan()
                    && user.getObservations() != null
                    && !user.getObservations().isBlank()) {
                planAccessService.requirePro(org, "Observaciones");
            }
        }

        return userRepository.save(user);
    }


    // 🔹 Asignar cursos a un usuario existente
    @Override
    @Transactional
    public User assignCourses(Long userId, List<Long> courseIds) {

        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getCourses() == null) {
            user.setCourses(new HashSet<>());
        }

        user.getCourses().clear();

        if (courseIds != null && !courseIds.isEmpty()) {
            List<Course> courses = courseRepository.findAllById(courseIds);
            user.getCourses().addAll(courses);
        }

        return userRepository.save(user);
    }

    // 🔹 Actualizar datos de usuario
    @Override
    public void updateUser(Long id, User updatedUser) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (updatedUser.getFullName() != null && !updatedUser.getFullName().isBlank()) {
            user.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getRole() != null) {
            Organization targetOrg = updatedUser.getOrganization() != null
                    ? updatedUser.getOrganization()
                    : user.getOrganization();
            if (updatedUser.getRole() == Rol.INSTRUCTOR
                    && targetOrg != null
                    && (user.getRole() != Rol.INSTRUCTOR || !user.isActive())) {
                planAccessService.validateInstructorLimit(targetOrg, true);
            }
            if (updatedUser.getRole() == Rol.USER
                    && targetOrg != null
                    && (user.getRole() != Rol.USER || !user.isActive())) {
                planAccessService.validateActiveStudentLimit(targetOrg, true);
            }
            user.setRole(updatedUser.getRole());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getOrganization() != null) {
            user.setOrganization(updatedUser.getOrganization());
        }

        if (updatedUser.getObservations() != null) {
            Organization obsOrg = updatedUser.getOrganization() != null
                    ? updatedUser.getOrganization()
                    : user.getOrganization();
            if (obsOrg != null
                    && !obsOrg.isProPlan()
                    && !updatedUser.getObservations().isBlank()) {
                planAccessService.requirePro(obsOrg, "Observaciones");
            }
            user.setObservations(updatedUser.getObservations());
        }

        userRepository.save(user);
    }

    /**
     * 🔹 Crear un nuevo ADMIN dentro de una organización
     * Solo puede hacerlo un SUPER_ADMIN o un ADMIN de la misma organización.
     */
    @Override
    public User createAdminForOrganization(Long organizationId, User newAdminData) {
        // 1️⃣ Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        // 2️⃣ Validar permisos
        if (currentUser.getRole() != Rol.SUPER_ADMIN && currentUser.getRole() != Rol.ADMIN) {
            throw new RuntimeException("Solo los administradores pueden crear otros administradores");
        }

        // 3️⃣ Si es ADMIN, solo puede crear admin dentro de su organización
        if (currentUser.getRole() == Rol.ADMIN) {
            if (currentUser.getOrganization() == null ||
                    !currentUser.getOrganization().getId().equals(organizationId)) {
                throw new RuntimeException("No puedes crear administradores fuera de tu organización");
            }
        }

        // 4️⃣ Validar organización
        Organization org = organizationRepository.findByIdAndActiveTrue(organizationId)
                .orElseThrow(() -> new RuntimeException("Organización no encontrada"));

        // 5️⃣ Validar email único
        if (userRepository.existsByEmail(newAdminData.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + newAdminData.getEmail());
        }

        // 6️⃣ Validar y encriptar contraseña
        if (newAdminData.getPassword() == null || newAdminData.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        String encodedPassword = passwordEncoder.encode(newAdminData.getPassword());

        // 7️⃣ Crear nuevo administrador
        User admin = User.builder()
                .fullName(newAdminData.getFullName())
                .email(newAdminData.getEmail())
                .password(encodedPassword)
                .role(Rol.ADMIN)
                .organization(org)
                .build();

        return userRepository.save(admin);
    }
    @Override
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 🔥 Actualizamos solo los campos editables
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        Rol newRole = Rol.valueOf(dto.getRole());
        Organization targetOrg = user.getOrganization();
        if (dto.getOrganizationId() != null) {
            targetOrg = organizationRepository.findByIdAndActiveTrue(dto.getOrganizationId())
                    .orElseThrow(() -> new RuntimeException("Organización no encontrada"));
        }
        if (newRole == Rol.INSTRUCTOR
                && targetOrg != null
                && (user.getRole() != Rol.INSTRUCTOR || !user.isActive())) {
            planAccessService.validateInstructorLimit(targetOrg, true);
        }
        if (newRole == Rol.USER
                && targetOrg != null
                && (user.getRole() != Rol.USER || !user.isActive())) {
            planAccessService.validateActiveStudentLimit(targetOrg, true);
        }
        user.setRole(newRole);

        if (dto.getOrganizationId() != null) {
            user.setOrganization(targetOrg);
        }

        if (dto.getObservations() != null) {
            if (targetOrg != null
                    && !targetOrg.isProPlan()
                    && !dto.getObservations().isBlank()) {
                planAccessService.requirePro(targetOrg, "Observaciones");
            }
            user.setObservations(dto.getObservations());
        }


        // Guardamos cambios
        User saved = userRepository.save(user);

        // Devolvemos DTO actualizado
        return new UserDTO(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.isActive(),
                saved.getObservations(),
                saved.getOrganization() != null ? saved.getOrganization().getName() : null,
                saved.getOrganization() != null ? saved.getOrganization().isProPlan() : false,
                saved.getCourses() != null ? saved.getCourses().stream().map(c -> c.getName()).toList() : List.of(),
                saved.getOrganization() != null ? saved.getOrganization().getId() : null
        );
    }
    @Override
    public Page<User> filterUsers(String search, String role, Long orgId, Long courseId, Boolean active, Pageable pageable) {
        Rol roleEnum = null;

        if (role != null && !role.isBlank() && !role.equals("ALL")) {
            roleEnum = Rol.valueOf(role);
        }

        return userRepository.filterUsers(search, roleEnum, orgId, courseId, active, pageable);
    }

    @Override
    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        String email = authentication.getName();

        return userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (user.getOrganization() != null && user.getRole() == Rol.USER) {
            planAccessService.validateActiveStudentLimit(user.getOrganization(), true);
        }
        if (user.getOrganization() != null && user.getRole() == Rol.INSTRUCTOR) {
            planAccessService.validateInstructorLimit(user.getOrganization(), true);
        }
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserImportResultDTO importUsersFromFile(MultipartFile file) {
        UserImportResultDTO result = UserImportResultDTO.builder().build();

        if (file == null || file.isEmpty()) {
            result.getErrors().add("Archivo vacío.");
            return result;
        }

        User currentUser = getAuthenticatedUser();
        Rol currentRole = currentUser.getRole();
        if (currentRole != Rol.SUPER_ADMIN && currentRole != Rol.ADMIN) {
            result.getErrors().add("Sin permisos para importar usuarios.");
            return result;
        }

        if (currentRole == Rol.ADMIN && currentUser.getOrganization() != null) {
            planAccessService.requirePro(currentUser.getOrganization(), "Importar usuarios desde Excel");
        }

        List<ImportRow> rows;
        try {
            rows = readImportRows(file);
        } catch (Exception e) {
            result.getErrors().add(e.getMessage() != null ? e.getMessage() : "No se pudo leer el archivo.");
            return result;
        }

        if (rows.isEmpty()) {
            result.getErrors().add("El archivo no contiene filas.");
            return result;
        }

        Organization defaultOrg = currentRole == Rol.ADMIN ? currentUser.getOrganization() : null;
        if (currentRole == Rol.ADMIN && defaultOrg == null) {
            result.getErrors().add("El admin no tiene organización asignada.");
            return result;
        }

        for (ImportRow row : rows) {
            if (row.isEmpty()) {
                continue;
            }

            result.setTotalRows(result.getTotalRows() + 1);

            String fullName = row.getValue(ImportField.FULL_NAME);
            String email = normalizeEmail(row.getValue(ImportField.EMAIL));
            String roleRaw = row.getValue(ImportField.ROLE);
            String coursesRaw = row.getValue(ImportField.COURSES);
            String orgRaw = row.getValue(ImportField.ORGANIZATION);

            if (fullName == null || fullName.isBlank()) {
                result.getErrors().add(formatRowError(row.rowNumber, "Nombre vacío."));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            if (email == null || email.isBlank() || !email.contains("@")) {
                result.getErrors().add(formatRowError(row.rowNumber, "Email inválido."));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            if (userRepository.existsByEmail(email)) {
                result.getErrors().add(formatRowError(row.rowNumber, "Email ya existe: " + email));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            Rol role = parseRole(roleRaw);
            if (role == Rol.SUPER_ADMIN && currentRole != Rol.SUPER_ADMIN) {
                result.getErrors().add(formatRowError(row.rowNumber, "No puedes crear SUPER_ADMIN."));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            Organization organization = resolveOrganization(currentRole, defaultOrg, orgRaw);
            if (currentRole == Rol.SUPER_ADMIN && orgRaw != null && organization == null) {
                result.getErrors().add(formatRowError(row.rowNumber, "Organización no encontrada: " + orgRaw));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }
            if (currentRole == Rol.SUPER_ADMIN && organization == null) {
                result.getErrors().add(formatRowError(row.rowNumber, "Debe indicar una organización para importar."));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }
            if (organization != null && !organization.isProPlan()) {
                result.getErrors().add(formatRowError(row.rowNumber, "Plan PRO requerido para importar usuarios."));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            String generatedPassword = generatePassword();
            User newUser = User.builder()
                    .fullName(fullName.trim())
                    .email(email)
                    .password(generatedPassword)
                    .role(role)
                    .organization(organization)
                    .build();

            User saved;
            try {
                saved = save(newUser);
            } catch (Exception ex) {
                String message = ex.getMessage() != null ? ex.getMessage() : "No se pudo crear el usuario.";
                result.getErrors().add(formatRowError(row.rowNumber, message));
                result.setSkipped(result.getSkipped() + 1);
                continue;
            }

            List<Long> courseIds = resolveCourseIds(coursesRaw, organization, currentRole, row, result);
            if (!courseIds.isEmpty()) {
                try {
                    assignCourses(saved.getId(), courseIds);
                } catch (Exception ex) {
                    String message = ex.getMessage() != null ? ex.getMessage() : "No se pudieron asignar cursos.";
                    result.getErrors().add(formatRowError(row.rowNumber, message));
                }
            }

            result.setCreated(result.getCreated() + 1);
            result.getGeneratedPasswords().add(
                    GeneratedPasswordDTO.builder()
                            .email(email)
                            .password(generatedPassword)
                            .build()
            );
        }

        return result;
    }

    private enum ImportField {
        FULL_NAME,
        EMAIL,
        ROLE,
        COURSES,
        ORGANIZATION
    }

    private static class ImportRow {
        private final int rowNumber;
        private final Map<ImportField, String> values;

        private ImportRow(int rowNumber, Map<ImportField, String> values) {
            this.rowNumber = rowNumber;
            this.values = values;
        }

        private String getValue(ImportField field) {
            return values.get(field);
        }

        private boolean isEmpty() {
            return values.values().stream().allMatch(v -> v == null || v.isBlank());
        }
    }

    private List<ImportRow> readImportRows(MultipartFile file) throws Exception {
        String filename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase(Locale.ROOT);
        if (filename.endsWith(".csv")) {
            return readCsvRows(file);
        }
        return readExcelRows(file);
    }

    private List<ImportRow> readExcelRows(MultipartFile file) throws Exception {
        List<ImportRow> rows = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return rows;
            }

            Row headerRow = sheet.getRow(sheet.getFirstRowNum());
            if (headerRow == null) {
                return rows;
            }

            Map<Integer, ImportField> columnMap = resolveHeaderMap(headerRow);
            if (!columnMap.containsValue(ImportField.FULL_NAME)
                    || !columnMap.containsValue(ImportField.EMAIL)) {
                throw new IllegalArgumentException("Faltan columnas requeridas (nombre, email).");
            }

            for (int i = headerRow.getRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                Map<ImportField, String> values = new HashMap<>();
                for (Map.Entry<Integer, ImportField> entry : columnMap.entrySet()) {
                    Cell cell = row.getCell(entry.getKey());
                    String value = cell != null ? DATA_FORMATTER.formatCellValue(cell) : "";
                    values.put(entry.getValue(), value != null ? value.trim() : "");
                }

                rows.add(new ImportRow(i + 1, values));
            }
        }

        return rows;
    }

    private List<ImportRow> readCsvRows(MultipartFile file) throws Exception {
        List<ImportRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) {
                return rows;
            }

            List<String> headers = splitCsvLine(headerLine);
            Map<Integer, ImportField> columnMap = resolveHeaderMap(headers);
            if (!columnMap.containsValue(ImportField.FULL_NAME)
                    || !columnMap.containsValue(ImportField.EMAIL)) {
                throw new IllegalArgumentException("Faltan columnas requeridas (nombre, email).");
            }

            String line;
            int rowNumber = 2;
            while ((line = reader.readLine()) != null) {
                List<String> valuesList = splitCsvLine(line);
                Map<ImportField, String> values = new HashMap<>();

                for (Map.Entry<Integer, ImportField> entry : columnMap.entrySet()) {
                    int index = entry.getKey();
                    String value = index < valuesList.size() ? valuesList.get(index) : "";
                    values.put(entry.getValue(), value != null ? value.trim() : "");
                }

                rows.add(new ImportRow(rowNumber, values));
                rowNumber++;
            }
        }

        return rows;
    }

    private Map<Integer, ImportField> resolveHeaderMap(Row headerRow) {
        Map<Integer, ImportField> columnMap = new HashMap<>();

        for (Cell cell : headerRow) {
            String header = cell != null ? DATA_FORMATTER.formatCellValue(cell) : "";
            ImportField field = mapHeaderToField(header);
            if (field != null) {
                columnMap.put(cell.getColumnIndex(), field);
            }
        }

        return columnMap;
    }

    private Map<Integer, ImportField> resolveHeaderMap(List<String> headers) {
        Map<Integer, ImportField> columnMap = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            ImportField field = mapHeaderToField(headers.get(i));
            if (field != null) {
                columnMap.put(i, field);
            }
        }
        return columnMap;
    }

    private ImportField mapHeaderToField(String header) {
        String normalized = normalizeHeader(header);
        if (normalized.isBlank()) {
            return null;
        }

        if (normalized.equals("nombre")
                || normalized.equals("nombre completo")
                || normalized.equals("full name")
                || normalized.equals("fullname")
                || normalized.equals("name")) {
            return ImportField.FULL_NAME;
        }

        if (normalized.equals("email")
                || normalized.equals("correo")
                || normalized.equals("correo electronico")) {
            return ImportField.EMAIL;
        }

        if (normalized.equals("rol") || normalized.equals("role")) {
            return ImportField.ROLE;
        }

        if (normalized.equals("cursos")
                || normalized.equals("curso")
                || normalized.equals("courses")
                || normalized.equals("course")) {
            return ImportField.COURSES;
        }

        if (normalized.equals("organizacion")
                || normalized.equals("organization")
                || normalized.equals("org")) {
            return ImportField.ORGANIZATION;
        }

        return null;
    }

    private String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = DIACRITICS.matcher(normalized).replaceAll("");
        normalized = normalized.trim().toLowerCase(Locale.ROOT);
        normalized = normalized.replace("_", " ").replace("-", " ");
        normalized = normalized.replaceAll("\\s+", " ");
        return normalized;
    }

    private String normalizeEmail(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private Rol parseRole(String value) {
        if (value == null || value.isBlank()) {
            return Rol.USER;
        }

        String normalized = normalizeHeader(value);
        return switch (normalized) {
            case "admin" -> Rol.ADMIN;
            case "super admin", "superadmin", "super_admin" -> Rol.SUPER_ADMIN;
            case "instructor", "profesor" -> Rol.INSTRUCTOR;
            case "usuario", "alumno", "user" -> Rol.USER;
            default -> Rol.USER;
        };
    }

    private Organization resolveOrganization(Rol currentRole, Organization defaultOrg, String orgRaw) {
        if (currentRole == Rol.ADMIN) {
            return defaultOrg;
        }

        if (orgRaw == null || orgRaw.isBlank()) {
            return null;
        }

        Organization org = organizationRepository.findByNameIgnoreCaseAndActiveTrue(orgRaw.trim()).orElse(null);
        if (org != null) {
            return org;
        }

        return findOrganizationByNormalizedName(orgRaw.trim());
    }

    private List<Long> resolveCourseIds(
            String coursesRaw,
            Organization organization,
            Rol currentRole,
            ImportRow row,
            UserImportResultDTO result
    ) {
        List<Long> courseIds = new ArrayList<>();
        if (coursesRaw == null || coursesRaw.isBlank()) {
            return courseIds;
        }

        String[] parts = coursesRaw.split("[,;]");
        for (String part : parts) {
            String courseName = part != null ? part.trim() : "";
            if (courseName.isBlank()) {
                continue;
            }

            Optional<Course> courseOpt;
            if (organization != null) {
                courseOpt = courseRepository.findByNameIgnoreCaseAndOrganizationIdAndActiveTrue(
                        courseName,
                        organization.getId()
                );
            } else {
                result.getErrors().add(formatRowError(row.rowNumber,
                        "Organización requerida para asignar cursos."));
                continue;
            }

            if (courseOpt.isEmpty()) {
                List<Course> matches = findCoursesByNormalizedName(courseName, organization);
                if (matches.isEmpty()) {
                    result.getErrors().add(formatRowError(row.rowNumber, "Curso no encontrado: " + courseName));
                    continue;
                }
                if (matches.size() > 1) {
                    result.getErrors().add(formatRowError(row.rowNumber,
                            "Curso duplicado, especifica organización: " + courseName));
                    continue;
                }
                courseIds.add(matches.get(0).getId());
                continue;
            }

            courseIds.add(courseOpt.get().getId());
        }

        return courseIds;
    }

    private List<Course> findCoursesByNormalizedName(String courseName, Organization organization) {
        if (courseName == null || courseName.isBlank()) {
            return List.of();
        }

        List<Course> candidates;
        if (organization != null) {
            candidates = courseRepository.findByOrganizationAndActiveTrue(organization);
        } else {
            return List.of();
        }

        String target = normalizeCourseName(courseName);
        List<Course> matches = new ArrayList<>();
        for (Course course : candidates) {
            if (normalizeCourseName(course.getName()).equals(target)) {
                matches.add(course);
            }
        }

        return matches;
    }

    private String normalizeCourseName(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        normalized = DIACRITICS.matcher(normalized).replaceAll("");
        normalized = normalized.trim().toLowerCase(Locale.ROOT);
        normalized = normalized.replaceAll("\\s+", " ");
        return normalized;
    }

    private Organization findOrganizationByNormalizedName(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String target = normalizeCourseName(value);
        List<Organization> organizations = organizationRepository.findByActiveTrue();
        for (Organization org : organizations) {
            if (normalizeCourseName(org.getName()).equals(target)) {
                return org;
            }
        }
        return null;
    }

    private String generatePassword() {
        StringBuilder builder = new StringBuilder(GENERATED_PASSWORD_LENGTH);
        for (int i = 0; i < GENERATED_PASSWORD_LENGTH; i++) {
            int index = RANDOM.nextInt(PASSWORD_CHARS.length());
            builder.append(PASSWORD_CHARS.charAt(index));
        }
        return builder.toString();
    }

    private String formatRowError(int rowNumber, String message) {
        return "Fila " + rowNumber + ": " + message;
    }

    private List<String> splitCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        if (line == null) {
            return tokens;
        }

        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (c == ',' && !inQuotes) {
                tokens.add(current.toString());
                current.setLength(0);
                continue;
            }

            current.append(c);
        }

        tokens.add(current.toString());
        return tokens;
    }
}
