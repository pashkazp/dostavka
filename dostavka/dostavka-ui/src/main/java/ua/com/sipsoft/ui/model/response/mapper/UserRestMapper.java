package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.ui.model.response.user.UserRest;

@Mapper(componentModel = "spring")
@Component
public interface UserRestMapper {

	UserRest toRest(UserDto userDto);

	List<UserRest> toRest(List<UserDto> users);

	Stream<UserRest> toRest(Stream<UserDto> users);

}
