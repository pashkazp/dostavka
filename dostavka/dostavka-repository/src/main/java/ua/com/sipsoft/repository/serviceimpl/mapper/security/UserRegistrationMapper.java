package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;

@Mapper(componentModel = "spring")
@Component
public interface UserRegistrationMapper {

//	@Mapping(target = "password", ignore = true)
	UserRegistrationDto toDto(User user);

	User fromDto(UserRegistrationDto userRegistrationDto);

}
