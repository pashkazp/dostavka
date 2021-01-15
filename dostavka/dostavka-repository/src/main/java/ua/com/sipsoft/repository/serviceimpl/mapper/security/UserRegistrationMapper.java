package ua.com.sipsoft.repository.serviceimpl.mapper.security;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ua.com.sipsoft.dao.user.User;
import ua.com.sipsoft.service.dto.user.UserRegistrationDto;

@Mapper
public interface UserRegistrationMapper {

	UserRegistrationMapper MAPPER = Mappers.getMapper(UserRegistrationMapper.class);

//	@Mapping(target = "password", ignore = true)
	UserRegistrationDto toDto(User user);

	User fromDto(UserRegistrationDto userRegistrationDto);

}
