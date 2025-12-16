package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.EnrollmentDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Enrollment;
import com.marcedev.attendance.entities.User;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-15T18:24:42-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.8 (Homebrew)"
)
@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public EnrollmentDTO toDTO(Enrollment enrollment) {
        if ( enrollment == null ) {
            return null;
        }

        Long userId = null;
        String userName = null;
        Long courseId = null;
        String courseName = null;
        Long id = null;
        boolean active = false;
        LocalDate startDate = null;

        userId = enrollmentUserId( enrollment );
        userName = enrollmentUserFullName( enrollment );
        courseId = enrollmentCourseId( enrollment );
        courseName = enrollmentCourseName( enrollment );
        id = enrollment.getId();
        active = enrollment.isActive();
        startDate = enrollment.getStartDate();

        EnrollmentDTO enrollmentDTO = new EnrollmentDTO( id, userId, userName, courseId, courseName, active, startDate );

        return enrollmentDTO;
    }

    private Long enrollmentUserId(Enrollment enrollment) {
        User user = enrollment.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String enrollmentUserFullName(Enrollment enrollment) {
        User user = enrollment.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFullName();
    }

    private Long enrollmentCourseId(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getId();
    }

    private String enrollmentCourseName(Enrollment enrollment) {
        Course course = enrollment.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getName();
    }
}
