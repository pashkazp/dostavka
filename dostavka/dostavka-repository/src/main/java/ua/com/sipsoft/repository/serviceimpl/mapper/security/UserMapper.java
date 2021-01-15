package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import java.util.List;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserDto;

@Mapper
public interface UserMapper {

	UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

//	@Mapping(target = "password", ignore = true)
	UserDto toDto(User user);

//	@Mapping(target = "password", ignore = true)
	User getCopy(User user);

	User fromDto(UserDto userDto);

	List<UserDto> toDto(List<User> user);

	List<User> fromDto(List<UserDto> userDto);

	Stream<UserDto> toDto(Stream<User> user);

	Stream<User> fromDto(Stream<UserDto> userDto);

}
