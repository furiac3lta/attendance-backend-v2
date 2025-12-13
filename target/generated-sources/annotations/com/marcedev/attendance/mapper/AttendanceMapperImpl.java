package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.AttendanceDTO;
import com.marcedev.attendance.entities.Attendance;
import com.marcedev.attendance.entities.ClassSession;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import com.marcedev.attendance.enums.Rol;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-13T18:12:04-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.8 (Homebrew)"
)
@Component
public class AttendanceMapperImpl implements AttendanceMapper {

    @Override
    public AttendanceDTO toDTO(Attendance entity) {
        if ( entity == null ) {
            return null;
        }

        AttendanceDTO.AttendanceDTOBuilder attendanceDTO = AttendanceDTO.builder();

        attendanceDTO.classSessionId( entityClassSessionId( entity ) );
        attendanceDTO.className( entityClassSessionName( entity ) );
        attendanceDTO.studentId( entityStudentId( entity ) );
        attendanceDTO.studentName( entityStudentFullName( entity ) );
        attendanceDTO.courseId( entityCourseId( entity ) );
        attendanceDTO.courseName( entityCourseName( entity ) );
        attendanceDTO.organizationId( entityOrganizationId( entity ) );
        attendanceDTO.organizationName( entityOrganizationName( entity ) );
        attendanceDTO.takenByName( entityTakenByFullName( entity ) );
        Rol role = entityTakenByRole( entity );
        if ( role != null ) {
            attendanceDTO.takenByRole( role.name() );
        }
        attendanceDTO.takenAt( entity.getTakenAt() );
        attendanceDTO.id( entity.getId() );
        attendanceDTO.attended( entity.isAttended() );

        return attendanceDTO.build();
    }

    @Override
    public Attendance toEntity(AttendanceDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Attendance.AttendanceBuilder attendance = Attendance.builder();

        attendance.attended( dto.isAttended() );

        return attendance.build();
    }

    private Long entityClassSessionId(Attendance attendance) {
        ClassSession classSession = attendance.getClassSession();
        if ( classSession == null ) {
            return null;
        }
        return classSession.getId();
    }

    private String entityClassSessionName(Attendance attendance) {
        ClassSession classSession = attendance.getClassSession();
        if ( classSession == null ) {
            return null;
        }
        return classSession.getName();
    }

    private Long entityStudentId(Attendance attendance) {
        User student = attendance.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getId();
    }

    private String entityStudentFullName(Attendance attendance) {
        User student = attendance.getStudent();
        if ( student == null ) {
            return null;
        }
        return student.getFullName();
    }

    private Long entityCourseId(Attendance attendance) {
        Course course = attendance.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getId();
    }

    private String entityCourseName(Attendance attendance) {
        Course course = attendance.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getName();
    }

    private Long entityOrganizationId(Attendance attendance) {
        Organization organization = attendance.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getId();
    }

    private String entityOrganizationName(Attendance attendance) {
        Organization organization = attendance.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getName();
    }

    private String entityTakenByFullName(Attendance attendance) {
        User takenBy = attendance.getTakenBy();
        if ( takenBy == null ) {
            return null;
        }
        return takenBy.getFullName();
    }

    private Rol entityTakenByRole(Attendance attendance) {
        User takenBy = attendance.getTakenBy();
        if ( takenBy == null ) {
            return null;
        }
        return takenBy.getRole();
    }
}
