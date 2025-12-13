package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.ClassDTO;
import com.marcedev.attendance.entities.ClassSession;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-13T18:12:04-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.8 (Homebrew)"
)
@Component
public class ClassMapperImpl implements ClassMapper {

    @Override
    public ClassDTO toDto(ClassSession classSession) {
        if ( classSession == null ) {
            return null;
        }

        ClassDTO.ClassDTOBuilder classDTO = ClassDTO.builder();

        classDTO.courseId( classSessionCourseId( classSession ) );
        classDTO.courseName( classSessionCourseName( classSession ) );
        classDTO.instructorId( classSessionInstructorId( classSession ) );
        classDTO.instructorName( classSessionInstructorFullName( classSession ) );
        Long id2 = classSessionOrganizationId( classSession );
        if ( id2 != null ) {
            classDTO.organizationId( id2 );
        }
        else {
            classDTO.organizationId( 0L );
        }
        String name1 = classSessionOrganizationName( classSession );
        if ( name1 != null ) {
            classDTO.organizationName( name1 );
        }
        else {
            classDTO.organizationName( "Sin organización" );
        }
        classDTO.id( classSession.getId() );
        classDTO.name( classSession.getName() );
        classDTO.date( classSession.getDate() );

        return classDTO.build();
    }

    private Long classSessionCourseId(ClassSession classSession) {
        Course course = classSession.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getId();
    }

    private String classSessionCourseName(ClassSession classSession) {
        Course course = classSession.getCourse();
        if ( course == null ) {
            return null;
        }
        return course.getName();
    }

    private Long classSessionInstructorId(ClassSession classSession) {
        User instructor = classSession.getInstructor();
        if ( instructor == null ) {
            return null;
        }
        return instructor.getId();
    }

    private String classSessionInstructorFullName(ClassSession classSession) {
        User instructor = classSession.getInstructor();
        if ( instructor == null ) {
            return null;
        }
        return instructor.getFullName();
    }

    private Long classSessionOrganizationId(ClassSession classSession) {
        Organization organization = classSession.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getId();
    }

    private String classSessionOrganizationName(ClassSession classSession) {
        Organization organization = classSession.getOrganization();
        if ( organization == null ) {
            return null;
        }
        return organization.getName();
    }
}
