package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.CourseDTO;
import com.marcedev.attendance.entities.Course;
import com.marcedev.attendance.entities.Organization;
import com.marcedev.attendance.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-13T18:12:04-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.8 (Homebrew)"
)
@Component
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseDTO toDTO(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseDTO.CourseDTOBuilder courseDTO = CourseDTO.builder();

        courseDTO.id( course.getId() );
        courseDTO.name( course.getName() );
        courseDTO.description( course.getDescription() );
        courseDTO.universityProgram( course.getUniversityProgram() );

        courseDTO.instructorId( course.getInstructor() != null ? course.getInstructor().getId() : null );
        courseDTO.instructorName( course.getInstructor() != null ? course.getInstructor().getFullName() : "Sin asignar" );
        courseDTO.organizationId( course.getOrganization() != null ? course.getOrganization().getId() : null );
        courseDTO.organizationName( course.getOrganization() != null ? course.getOrganization().getName() : "Sin organización" );

        return courseDTO.build();
    }

    @Override
    public List<CourseDTO> toDTOList(List<Course> courses) {
        if ( courses == null ) {
            return null;
        }

        List<CourseDTO> list = new ArrayList<CourseDTO>( courses.size() );
        for ( Course course : courses ) {
            list.add( toDTO( course ) );
        }

        return list;
    }

    @Override
    public Course toEntity(CourseDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Course.CourseBuilder course = Course.builder();

        course.instructor( courseDTOToUser( dto ) );
        course.organization( courseDTOToOrganization( dto ) );
        course.id( dto.getId() );
        course.name( dto.getName() );
        course.description( dto.getDescription() );
        course.universityProgram( dto.getUniversityProgram() );

        return course.build();
    }

    protected User courseDTOToUser(CourseDTO courseDTO) {
        if ( courseDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( courseDTO.getInstructorId() );

        return user.build();
    }

    protected Organization courseDTOToOrganization(CourseDTO courseDTO) {
        if ( courseDTO == null ) {
            return null;
        }

        Organization.OrganizationBuilder organization = Organization.builder();

        organization.id( courseDTO.getOrganizationId() );
        organization.name( courseDTO.getOrganizationName() );

        return organization.build();
    }
}
