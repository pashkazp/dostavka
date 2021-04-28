package ua.com.sipsoft.service.security;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserDto;

@Mapper(componentModel = "spring")
@Component
public interface UserDtoMapper {
	UserDto getUserDto(User user);
}