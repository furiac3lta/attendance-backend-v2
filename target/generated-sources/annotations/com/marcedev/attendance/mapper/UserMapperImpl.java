package com.marcedev.attendance.mapper;

import com.marcedev.attendance.dto.UserDTO;
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
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setFullName( user.getFullName() );
        userDTO.setEmail( user.getEmail() );
        if ( user.getRole() != null ) {
            userDTO.setRole( user.getRole().name() );
        }

        userDTO.setOrganizationName( user.getOrganization() != null ? user.getOrganization().getName() : null );
        userDTO.setOrganizationId( user.getOrganization() != null ? user.getOrganization().getId() : null );
        userDTO.setCourses( mapCourses(user.getCourses()) );

        return userDTO;
    }

    @Override
    public List<UserDTO> toDTOList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>( users.size() );
        for ( User user : users ) {
            list.add( toDTO( user ) );
        }

        return list;
    }
}
