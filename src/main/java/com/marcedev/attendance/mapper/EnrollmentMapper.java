package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.EnrollmentDTO;
import com.marcedev.attendance.entities.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.name", target = "courseName")
    EnrollmentDTO toDTO(Enrollment enrollment);
}
