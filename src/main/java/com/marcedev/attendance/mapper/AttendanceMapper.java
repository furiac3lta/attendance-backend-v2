package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.AttendanceDTO;
import com.marcedev.attendance.entities.Attendance;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    // Entity → DTO
    @Mapping(source = "classSession.id", target = "classSessionId")
    @Mapping(source = "classSession.name", target = "className")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "takenBy.fullName", target = "takenByName")
    @Mapping(source = "takenBy.role", target = "takenByRole")
    @Mapping(source = "takenAt", target = "takenAt")
    AttendanceDTO toDTO(Attendance entity);

    // DTO → Entity (solo campos simples)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "classSession", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "takenBy", ignore = true)
    @Mapping(target = "takenAt", ignore = true)
    Attendance toEntity(AttendanceDTO dto);
}
