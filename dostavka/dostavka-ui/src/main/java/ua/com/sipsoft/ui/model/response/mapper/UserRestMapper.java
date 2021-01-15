package ua.com.sipsoft.ui.model.response.mapper;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.service.dto.user.UserDto;
import ua.com.sipsoft.ui.model.response.user.UserRest;

@Mapper
public interface UserRestMapper {

	UserRestMapper MAPPER = Mappers.getMapper(UserRestMapper.class);

	UserRest toRest(UserDto userDto);

	List<UserRest> toRest(List<UserDto> users);

	Stream<UserRest> toRest(Stream<UserDto> users);

}
